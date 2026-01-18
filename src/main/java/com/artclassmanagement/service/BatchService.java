package com.artclassmanagement.service;

import com.artclassmanagement.dto.request.BatchRequestDto;
import com.artclassmanagement.dto.response.BatchResponseDto;
import com.artclassmanagement.enums.BatchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BatchService {
    BatchResponseDto create(BatchRequestDto request);

    BatchResponseDto getById(String id);

    Page<BatchResponseDto> getAll(Pageable pageable);

    Page<BatchResponseDto> getByClassId(String classId, Pageable pageable);

    Page<BatchResponseDto> getByInstructorId(String instructorId, Pageable pageable);

    Page<BatchResponseDto> getByStatus(BatchStatus status, Pageable pageable);

    BatchResponseDto update(String id, BatchRequestDto request);

    BatchResponseDto updateStatus(String id, BatchStatus status);

    void delete(String id);
}
