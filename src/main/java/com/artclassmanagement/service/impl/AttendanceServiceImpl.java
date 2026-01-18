package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.request.AttendanceRequestDto;
import com.artclassmanagement.dto.response.AttendanceResponseDto;
import com.artclassmanagement.entity.*;
import com.artclassmanagement.enums.AttendanceStatus;
import com.artclassmanagement.exception.AccessDeniedException;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.mapper.AttendanceMapper;
import com.artclassmanagement.repository.*;
import com.artclassmanagement.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ClassSessionRepository sessionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final AttendanceMapper attendanceMapper;

    @Override
    public List<AttendanceResponseDto> markAttendance(AttendanceRequestDto request) {
        User currentUser = getCurrentUser();
        log.info("Marking attendance for session: {} by instructor: {}", request.getSessionId(), currentUser.getId());

        ClassSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession", "id", request.getSessionId()));

        // Verify instructor access
        Instructor instructor = instructorRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new AccessDeniedException("Only instructors can mark attendance"));

        if (!session.getInstructorId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only mark attendance for your own sessions");
        }

        List<Attendance> attendanceRecords = new ArrayList<>();
        int presentCount = 0;
        int absentCount = 0;

        for (AttendanceRequestDto.StudentAttendance record : request.getAttendanceRecords()) {
            Attendance attendance = attendanceRepository.findBySessionIdAndStudentId(
                    request.getSessionId(), record.getStudentId())
                    .orElse(new Attendance());

            User student = userRepository.findById(record.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", record.getStudentId()));

            attendance.setSessionId(request.getSessionId());
            attendance.setBatchId(session.getBatchId());
            attendance.setStudentId(record.getStudentId());
            attendance.setStudentName(student.getFullName());
            attendance.setStudentEmail(student.getEmail());
            attendance.setStatus(record.getStatus());
            attendance.setMarkedAt(Instant.now());
            attendance.setMarkedBy(currentUser.getId());
            attendance.setMarkedByName(instructor.getDisplayName());
            attendance.setRemarks(record.getRemarks());

            if (record.getStatus() == AttendanceStatus.LATE) {
                attendance.setLateByMinutes(record.getLateByMinutes());
            }

            if (record.getStatus() == AttendanceStatus.PRESENT || record.getStatus() == AttendanceStatus.LATE) {
                presentCount++;
            } else {
                absentCount++;
            }

            attendanceRecords.add(attendanceRepository.save(attendance));
        }

        // Update session summary
        session.updateAttendanceSummary(presentCount, absentCount, request.getAttendanceRecords().size());
        sessionRepository.save(session);

        log.info("Marked attendance for {} students in session {}", attendanceRecords.size(), request.getSessionId());
        return attendanceRecords.stream().map(attendanceMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceResponseDto> getBySessionId(String sessionId) {
        return attendanceRepository.findBySessionId(sessionId).stream()
                .map(attendanceMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<AttendanceResponseDto> getByStudentId(String studentId, Pageable pageable) {
        return attendanceRepository.findByStudentId(studentId, pageable).map(attendanceMapper::toDto);
    }

    @Override
    public Page<AttendanceResponseDto> getMyAttendance(Pageable pageable) {
        User currentUser = getCurrentUser();
        return attendanceRepository.findByStudentId(currentUser.getId(), pageable).map(attendanceMapper::toDto);
    }

    @Override
    public List<AttendanceResponseDto> getStudentBatchAttendance(String studentId, String batchId) {
        return attendanceRepository.findByStudentIdAndBatchId(studentId, batchId).stream()
                .map(attendanceMapper::toDto).collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
