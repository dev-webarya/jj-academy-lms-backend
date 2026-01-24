package com.artclassmanagement.service;

import com.artclassmanagement.dto.request.AttendanceRequestDto;
import com.artclassmanagement.dto.response.AttendanceResponseDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service for attendance management.
 * Tracks session attendance with over-limit detection.
 */
public interface AttendanceService {

    /**
     * Mark attendance for a session.
     * Returns updated session with attendance records including over-limit flags.
     */
    ClassSessionResponseDto markAttendance(AttendanceRequestDto request);

    /**
     * Get all attendance records for a session.
     */
    List<AttendanceResponseDto> getBySessionId(String sessionId);

    /**
     * Get attendance history for a student.
     */
    Page<AttendanceResponseDto> getByStudentId(String studentId, Pageable pageable);

    /**
     * Get current user's attendance history.
     */
    Page<AttendanceResponseDto> getMyAttendance(Pageable pageable);

    /**
     * Get students who are over their monthly session limit.
     * Returns list of students with their session counts.
     */
    List<AttendanceResponseDto> getOverLimitStudents(Integer month, Integer year);

    /**
     * Get monthly attendance summary for a student.
     */
    AttendanceResponseDto getStudentMonthlyAttendance(String studentId, Integer month, Integer year);
}
