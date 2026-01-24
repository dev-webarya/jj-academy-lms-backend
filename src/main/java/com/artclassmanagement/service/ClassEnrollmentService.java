package com.artclassmanagement.service;

import com.artclassmanagement.dto.response.ClassEnrollmentResponseDto;
import com.artclassmanagement.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service for managing ClassEnrollments from ArtAcademy.
 * Simplified - no batch assignment needed.
 */
public interface ClassEnrollmentService {

    /**
     * Get all enrollments (paginated).
     */
    Page<ClassEnrollmentResponseDto> getAllEnrollments(Pageable pageable);

    /**
     * Get enrollments by status (e.g., PENDING for new enrollments).
     */
    Page<ClassEnrollmentResponseDto> getByStatus(EnrollmentStatus status, Pageable pageable);

    /**
     * Get enrollments by class ID.
     */
    Page<ClassEnrollmentResponseDto> getByClassId(String classId, Pageable pageable);

    /**
     * Get single enrollment by ID.
     */
    ClassEnrollmentResponseDto getById(String id);

    /**
     * Approve an enrollment.
     */
    ClassEnrollmentResponseDto approveEnrollment(String enrollmentId, String adminNotes);

    /**
     * Reject an enrollment.
     */
    ClassEnrollmentResponseDto rejectEnrollment(String enrollmentId, String adminNotes);

    /**
     * Update enrollment status only.
     */
    ClassEnrollmentResponseDto updateStatus(String id, EnrollmentStatus status, String adminNotes);

    /**
     * Count pending enrollments.
     */
    long countPendingEnrollments();
}
