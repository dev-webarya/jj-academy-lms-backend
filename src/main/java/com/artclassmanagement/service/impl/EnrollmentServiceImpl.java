package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.request.AssignBatchRequestDto;
import com.artclassmanagement.dto.request.EnrollmentRequestDto;
import com.artclassmanagement.dto.response.EnrollmentResponseDto;
import com.artclassmanagement.entity.Batch;
import com.artclassmanagement.entity.Enrollment;
import com.artclassmanagement.entity.User;
import com.artclassmanagement.enums.EnrollmentStatus;
import com.artclassmanagement.exception.DuplicateResourceException;
import com.artclassmanagement.exception.InvalidRequestException;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.mapper.EnrollmentMapper;
import com.artclassmanagement.repository.BatchRepository;
import com.artclassmanagement.repository.EnrollmentRepository;
import com.artclassmanagement.repository.UserRepository;
import com.artclassmanagement.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final BatchRepository batchRepository;
    private final UserRepository userRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public EnrollmentResponseDto enroll(EnrollmentRequestDto request) {
        User currentUser = getCurrentUser();
        log.info("User {} enrolling in class: {}", currentUser.getId(), request.getClassId());

        // Check for duplicate enrollment
        if (enrollmentRepository.existsByUserIdAndClassId(currentUser.getId(), request.getClassId())) {
            throw new DuplicateResourceException("You are already enrolled in this class");
        }

        // Verify user has STUDENT role
        if (!currentUser.getRoles().contains("ROLE_STUDENT")) {
            throw new InvalidRequestException("Only students can enroll in classes");
        }

        Enrollment enrollment = Enrollment.builder()
                .userId(currentUser.getId())
                .studentName(currentUser.getFullName())
                .studentEmail(currentUser.getEmail())
                .classId(request.getClassId())
                .className("Art Class") // TODO: Fetch from ArtAcademy
                .status(EnrollmentStatus.PENDING)
                .enrollmentDate(Instant.now())
                .notes(request.getNotes())
                .build();

        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponseDto getById(String id) {
        return enrollmentMapper.toDto(findById(id));
    }

    @Override
    public Page<EnrollmentResponseDto> getAll(Pageable pageable) {
        return enrollmentRepository.findAll(pageable).map(enrollmentMapper::toDto);
    }

    @Override
    public Page<EnrollmentResponseDto> getByUserId(String userId, Pageable pageable) {
        return enrollmentRepository.findByUserId(userId, pageable).map(enrollmentMapper::toDto);
    }

    @Override
    public Page<EnrollmentResponseDto> getByClassId(String classId, Pageable pageable) {
        return enrollmentRepository.findByClassId(classId, pageable).map(enrollmentMapper::toDto);
    }

    @Override
    public Page<EnrollmentResponseDto> getByBatchId(String batchId, Pageable pageable) {
        return enrollmentRepository.findByBatchId(batchId, pageable).map(enrollmentMapper::toDto);
    }

    @Override
    public Page<EnrollmentResponseDto> getByStatus(EnrollmentStatus status, Pageable pageable) {
        return enrollmentRepository.findByStatus(status, pageable).map(enrollmentMapper::toDto);
    }

    @Override
    public Page<EnrollmentResponseDto> getMyEnrollments(Pageable pageable) {
        User currentUser = getCurrentUser();
        return enrollmentRepository.findByUserId(currentUser.getId(), pageable).map(enrollmentMapper::toDto);
    }

    @Override
    public EnrollmentResponseDto assignToBatch(String enrollmentId, AssignBatchRequestDto request) {
        log.info("Assigning enrollment {} to batch: {}", enrollmentId, request.getBatchId());

        Enrollment enrollment = findById(enrollmentId);
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", request.getBatchId()));

        if (!batch.hasAvailableSlots()) {
            throw new InvalidRequestException("Batch is full, no available slots");
        }

        enrollment.assignToBatch(batch.getId(), batch.getBatchName());
        batch.incrementStudentCount();

        batchRepository.save(batch);
        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponseDto updateStatus(String id, EnrollmentStatus status, String notes) {
        log.info("Updating enrollment {} status to: {}", id, status);
        Enrollment enrollment = findById(id);
        enrollment.setStatus(status);
        if (notes != null) {
            enrollment.setNotes(notes);
        }
        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public void delete(String id) {
        log.warn("Deleting enrollment: {}", id);
        Enrollment enrollment = findById(id);

        // Decrement batch count if assigned
        if (enrollment.getBatchId() != null) {
            batchRepository.findById(enrollment.getBatchId())
                    .ifPresent(batch -> {
                        batch.decrementStudentCount();
                        batchRepository.save(batch);
                    });
        }

        enrollmentRepository.delete(enrollment);
    }

    private Enrollment findById(String id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
