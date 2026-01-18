package com.artclassmanagement.service;

import com.artclassmanagement.dto.request.AssignBatchRequestDto;
import com.artclassmanagement.dto.request.EnrollmentRequestDto;
import com.artclassmanagement.dto.response.EnrollmentResponseDto;
import com.artclassmanagement.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnrollmentService {
    EnrollmentResponseDto enroll(EnrollmentRequestDto request); // For students

    EnrollmentResponseDto getById(String id);

    Page<EnrollmentResponseDto> getAll(Pageable pageable);

    Page<EnrollmentResponseDto> getByUserId(String userId, Pageable pageable);

    Page<EnrollmentResponseDto> getByClassId(String classId, Pageable pageable);

    Page<EnrollmentResponseDto> getByBatchId(String batchId, Pageable pageable);

    Page<EnrollmentResponseDto> getByStatus(EnrollmentStatus status, Pageable pageable);

    Page<EnrollmentResponseDto> getMyEnrollments(Pageable pageable);

    EnrollmentResponseDto assignToBatch(String enrollmentId, AssignBatchRequestDto request); // Admin

    EnrollmentResponseDto updateStatus(String id, EnrollmentStatus status, String notes); // Admin

    void delete(String id);
}
