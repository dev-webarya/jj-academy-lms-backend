package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.response.ClassEnrollmentResponseDto;
import com.artclassmanagement.entity.ClassEnrollment;
import com.artclassmanagement.enums.EnrollmentStatus;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.mapper.ClassEnrollmentMapper;
import com.artclassmanagement.repository.ClassEnrollmentRepository;
import com.artclassmanagement.service.ClassEnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing ClassEnrollments from ArtAcademy.
 * Simplified - no batch assignment needed.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClassEnrollmentServiceImpl implements ClassEnrollmentService {

    private final ClassEnrollmentRepository classEnrollmentRepository;
    private final ClassEnrollmentMapper classEnrollmentMapper;

    @Override
    public Page<ClassEnrollmentResponseDto> getAllEnrollments(Pageable pageable) {
        return classEnrollmentRepository.findAll(pageable).map(classEnrollmentMapper::toDto);
    }

    @Override
    public Page<ClassEnrollmentResponseDto> getByStatus(EnrollmentStatus status, Pageable pageable) {
        return classEnrollmentRepository.findByStatus(status, pageable).map(classEnrollmentMapper::toDto);
    }

    @Override
    public Page<ClassEnrollmentResponseDto> getByClassId(String classId, Pageable pageable) {
        return classEnrollmentRepository.findByClassId(classId, pageable).map(classEnrollmentMapper::toDto);
    }

    @Override
    public ClassEnrollmentResponseDto getById(String id) {
        return classEnrollmentMapper.toDto(findById(id));
    }

    @Override
    public ClassEnrollmentResponseDto approveEnrollment(String enrollmentId, String adminNotes) {
        log.info("Approving enrollment {}", enrollmentId);
        ClassEnrollment enrollment = findById(enrollmentId);
        enrollment.setStatus(EnrollmentStatus.APPROVED);
        if (adminNotes != null) {
            enrollment.setAdminNotes(adminNotes);
        }
        return classEnrollmentMapper.toDto(classEnrollmentRepository.save(enrollment));
    }

    @Override
    public ClassEnrollmentResponseDto rejectEnrollment(String enrollmentId, String adminNotes) {
        log.info("Rejecting enrollment {}", enrollmentId);
        ClassEnrollment enrollment = findById(enrollmentId);
        enrollment.setStatus(EnrollmentStatus.REJECTED);
        if (adminNotes != null) {
            enrollment.setAdminNotes(adminNotes);
        }
        return classEnrollmentMapper.toDto(classEnrollmentRepository.save(enrollment));
    }

    @Override
    public ClassEnrollmentResponseDto updateStatus(String id, EnrollmentStatus status, String adminNotes) {
        log.info("Updating enrollment {} status to {}", id, status);
        ClassEnrollment enrollment = findById(id);
        enrollment.setStatus(status);
        if (adminNotes != null) {
            enrollment.setAdminNotes(adminNotes);
        }
        return classEnrollmentMapper.toDto(classEnrollmentRepository.save(enrollment));
    }

    @Override
    public long countPendingEnrollments() {
        return classEnrollmentRepository.countByStatus(EnrollmentStatus.PENDING);
    }

    private ClassEnrollment findById(String id) {
        return classEnrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassEnrollment", "id", id));
    }
}
