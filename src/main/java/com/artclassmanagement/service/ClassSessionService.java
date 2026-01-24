package com.artclassmanagement.service;

import com.artclassmanagement.dto.request.ClassSessionRequestDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import com.artclassmanagement.enums.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing class sessions.
 * Admin-only operations.
 */
public interface ClassSessionService {

    // CRUD operations
    ClassSessionResponseDto create(ClassSessionRequestDto request);

    ClassSessionResponseDto getById(String id);

    ClassSessionResponseDto update(String id, ClassSessionRequestDto request);

    void delete(String id);

    // Query operations
    Page<ClassSessionResponseDto> getAll(Pageable pageable);

    List<ClassSessionResponseDto> getByDateRange(LocalDate startDate, LocalDate endDate);

    List<ClassSessionResponseDto> getByDate(LocalDate date);

    Page<ClassSessionResponseDto> getUpcoming(Pageable pageable);

    // Status management
    ClassSessionResponseDto updateStatus(String id, SessionStatus status, String reason);

    // Attendance-related
    ClassSessionResponseDto getSessionWithAttendance(String id);
}
