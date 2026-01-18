package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.request.ClassSessionRequestDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import com.artclassmanagement.entity.Batch;
import com.artclassmanagement.entity.ClassSession;
import com.artclassmanagement.entity.Instructor;
import com.artclassmanagement.entity.User;
import com.artclassmanagement.enums.SessionStatus;
import com.artclassmanagement.exception.AccessDeniedException;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.mapper.ClassSessionMapper;
import com.artclassmanagement.repository.BatchRepository;
import com.artclassmanagement.repository.ClassSessionRepository;
import com.artclassmanagement.repository.InstructorRepository;
import com.artclassmanagement.repository.UserRepository;
import com.artclassmanagement.service.ClassSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassSessionServiceImpl implements ClassSessionService {

    private final ClassSessionRepository sessionRepository;
    private final BatchRepository batchRepository;
    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final ClassSessionMapper sessionMapper;

    @Override
    public ClassSessionResponseDto create(ClassSessionRequestDto request) {
        log.info("Creating class session for batch: {}", request.getBatchId());

        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", request.getBatchId()));

        User currentUser = getCurrentUser();
        Instructor instructor = instructorRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new AccessDeniedException("Only instructors can create sessions"));

        // Verify instructor is assigned to this batch
        if (!batch.getInstructorId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not assigned to this batch");
        }

        ClassSession session = sessionMapper.toEntity(request);
        session.setBatchName(batch.getBatchName());
        session.setInstructorId(currentUser.getId());
        session.setInstructorName(instructor.getDisplayName());
        session.setStatus(SessionStatus.SCHEDULED);

        return sessionMapper.toDto(sessionRepository.save(session));
    }

    @Override
    public ClassSessionResponseDto getById(String id) {
        return sessionMapper.toDto(findById(id));
    }

    @Override
    public Page<ClassSessionResponseDto> getByBatchId(String batchId, Pageable pageable) {
        return sessionRepository.findByBatchId(batchId, pageable).map(sessionMapper::toDto);
    }

    @Override
    public Page<ClassSessionResponseDto> getByInstructorId(String instructorId, Pageable pageable) {
        return sessionRepository.findByInstructorId(instructorId, pageable).map(sessionMapper::toDto);
    }

    @Override
    public List<ClassSessionResponseDto> getByBatchIdAndDateRange(String batchId, LocalDate startDate,
            LocalDate endDate) {
        return sessionRepository.findByBatchIdAndSessionDateBetween(batchId, startDate, endDate)
                .stream().map(sessionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ClassSessionResponseDto update(String id, ClassSessionRequestDto request) {
        log.info("Updating session: {}", id);
        ClassSession session = findById(id);
        verifyInstructorAccess(session);
        sessionMapper.updateEntity(request, session);
        return sessionMapper.toDto(sessionRepository.save(session));
    }

    @Override
    public ClassSessionResponseDto updateStatus(String id, SessionStatus status, String reason) {
        log.info("Updating session {} status to: {}", id, status);
        ClassSession session = findById(id);
        verifyInstructorAccess(session);
        session.setStatus(status);
        if (status == SessionStatus.CANCELLED || status == SessionStatus.POSTPONED) {
            session.setCancellationReason(reason);
        }
        return sessionMapper.toDto(sessionRepository.save(session));
    }

    @Override
    public void delete(String id) {
        log.warn("Deleting session: {}", id);
        ClassSession session = findById(id);
        verifyInstructorAccess(session);
        sessionRepository.delete(session);
    }

    private ClassSession findById(String id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession", "id", id));
    }

    private void verifyInstructorAccess(ClassSession session) {
        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRoles().contains("ROLE_ADMIN");
        if (!isAdmin && !session.getInstructorId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only modify your own sessions");
        }
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
