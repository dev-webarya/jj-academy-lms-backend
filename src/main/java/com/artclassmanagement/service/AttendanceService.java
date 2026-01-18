package com.artclassmanagement.service;

import com.artclassmanagement.dto.request.AttendanceRequestDto;
import com.artclassmanagement.dto.response.AttendanceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttendanceService {
    List<AttendanceResponseDto> markAttendance(AttendanceRequestDto request); // Instructor

    List<AttendanceResponseDto> getBySessionId(String sessionId);

    Page<AttendanceResponseDto> getByStudentId(String studentId, Pageable pageable);

    Page<AttendanceResponseDto> getMyAttendance(Pageable pageable); // Student

    List<AttendanceResponseDto> getStudentBatchAttendance(String studentId, String batchId);
}
