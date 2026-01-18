package com.artclassmanagement.service;

import com.artclassmanagement.dto.request.ClassSessionRequestDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import com.artclassmanagement.enums.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ClassSessionService {
    ClassSessionResponseDto create(ClassSessionRequestDto request);

    ClassSessionResponseDto getById(String id);

    Page<ClassSessionResponseDto> getByBatchId(String batchId, Pageable pageable);

    Page<ClassSessionResponseDto> getByInstructorId(String instructorId, Pageable pageable);

    List<ClassSessionResponseDto> getByBatchIdAndDateRange(String batchId, LocalDate startDate, LocalDate endDate);

    ClassSessionResponseDto update(String id, ClassSessionRequestDto request);

    ClassSessionResponseDto updateStatus(String id, SessionStatus status, String reason);

    void delete(String id);
}
