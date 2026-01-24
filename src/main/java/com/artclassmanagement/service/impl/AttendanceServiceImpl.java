package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.request.AttendanceRequestDto;
import com.artclassmanagement.dto.response.AttendanceResponseDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import com.artclassmanagement.entity.*;
import com.artclassmanagement.enums.AttendanceStatus;
import com.artclassmanagement.enums.SubscriptionStatus;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.mapper.AttendanceMapper;
import com.artclassmanagement.mapper.ClassSessionMapper;
import com.artclassmanagement.repository.*;
import com.artclassmanagement.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for attendance management.
 * Tracks session attendance with subscription limits and over-limit detection.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private static final int DEFAULT_SESSION_LIMIT = 8;

    private final AttendanceRepository attendanceRepository;
    private final ClassSessionRepository sessionRepository;
    private final StudentSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final AttendanceMapper attendanceMapper;
    private final ClassSessionMapper sessionMapper;

    @Override
    public ClassSessionResponseDto markAttendance(AttendanceRequestDto request) {
        log.info("Marking attendance for session: {}", request.getSessionId());

        ClassSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession", "id", request.getSessionId()));

        LocalDate sessionDate = session.getSessionDate();
        int sessionMonth = sessionDate.getMonthValue();
        int sessionYear = sessionDate.getYear();

        List<ClassSession.SessionAttendanceRecord> sessionRecords = new ArrayList<>();
        int presentCount = 0;
        int absentCount = 0;

        for (AttendanceRequestDto.StudentAttendance record : request.getAttendanceRecords()) {
            User student = userRepository.findById(record.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", record.getStudentId()));

            // Find or get subscription for this month
            StudentSubscription subscription = subscriptionRepository
                    .findByStudentIdAndSubscriptionMonthAndSubscriptionYear(
                            record.getStudentId(), sessionMonth, sessionYear)
                    .orElse(null);

            // Get current session count for this month
            long currentMonthCount = attendanceRepository.countByStudentIdAndSessionMonthAndSessionYearAndStatus(
                    record.getStudentId(), sessionMonth, sessionYear, AttendanceStatus.PRESENT);

            // Determine if marking as present
            boolean isPresent = record.getIsPresent();
            AttendanceStatus status = isPresent ? AttendanceStatus.PRESENT : AttendanceStatus.ABSENT;

            // Calculate new session count if marking present
            int newSessionCount = (int) currentMonthCount;
            if (isPresent) {
                newSessionCount++;
            }

            // Check if over limit
            int limit = (subscription != null) ? subscription.getAllowedSessions() : DEFAULT_SESSION_LIMIT;
            boolean isOverLimit = newSessionCount > limit;

            // Create or update attendance record
            Attendance attendance = attendanceRepository.findBySessionIdAndStudentId(
                    request.getSessionId(), record.getStudentId())
                    .orElse(new Attendance());

            attendance.setSessionId(request.getSessionId());
            attendance.setStudentId(record.getStudentId());
            attendance.setStudentName(student.getFullName());
            attendance.setStudentEmail(student.getEmail());
            attendance.setSubscriptionId(subscription != null ? subscription.getId() : null);
            attendance.setSessionDate(sessionDate);
            attendance.setSessionMonth(sessionMonth);
            attendance.setSessionYear(sessionYear);
            attendance.setSessionCountThisMonth(newSessionCount);
            attendance.setIsOverLimit(isOverLimit);
            attendance.setStatus(status);
            attendance.setMarkedAt(Instant.now());
            attendance.setRemarks(record.getRemarks());

            attendanceRepository.save(attendance);

            // Update subscription attended count if present
            if (isPresent && subscription != null) {
                subscription.setAttendedSessions(newSessionCount);
                subscriptionRepository.save(subscription);
            }

            // Track counts
            if (isPresent) {
                presentCount++;
            } else {
                absentCount++;
            }

            // Create embedded record for session document
            ClassSession.SessionAttendanceRecord sessionRecord = ClassSession.SessionAttendanceRecord.builder()
                    .studentId(record.getStudentId())
                    .studentName(student.getFullName())
                    .studentEmail(student.getEmail())
                    .subscriptionId(subscription != null ? subscription.getId() : null)
                    .isPresent(isPresent)
                    .sessionCountThisMonth(newSessionCount)
                    .isOverLimit(isOverLimit)
                    .remarks(record.getRemarks())
                    .markedAt(Instant.now())
                    .build();

            sessionRecords.add(sessionRecord);

            if (isOverLimit) {
                log.warn("Student {} ({}) has exceeded session limit: {} sessions (limit: {})",
                        student.getFullName(), student.getEmail(), newSessionCount, limit);
            }
        }

        // Update session with attendance summary and records
        session.setAttendanceRecords(sessionRecords);
        session.updateAttendanceSummary(presentCount, absentCount, request.getAttendanceRecords().size());
        ClassSession savedSession = sessionRepository.save(session);

        log.info("Marked attendance for {} students in session {}. Present: {}, Absent: {}",
                request.getAttendanceRecords().size(), request.getSessionId(), presentCount, absentCount);

        return sessionMapper.toDto(savedSession);
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
    public List<AttendanceResponseDto> getOverLimitStudents(Integer month, Integer year) {
        log.info("Getting over-limit students for {}/{}", month, year);
        return attendanceRepository.findOverLimitByMonthAndYear(month, year).stream()
                .map(attendanceMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public AttendanceResponseDto getStudentMonthlyAttendance(String studentId, Integer month, Integer year) {
        List<Attendance> attendanceList = attendanceRepository
                .findByStudentIdAndSessionMonthAndSessionYear(studentId, month, year);

        if (attendanceList.isEmpty()) {
            return null;
        }

        // Return the most recent attendance record which has the latest count
        Attendance latest = attendanceList.get(attendanceList.size() - 1);
        return attendanceMapper.toDto(latest);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
