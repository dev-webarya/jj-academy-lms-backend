package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.request.ClassSessionRequestDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import com.artclassmanagement.entity.ClassSession;
import com.artclassmanagement.enums.SessionStatus;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.mapper.ClassSessionMapper;
import com.artclassmanagement.repository.ClassSessionRepository;
import com.artclassmanagement.service.ClassSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for class session management.
 * Admin creates and manages sessions.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClassSessionServiceImpl implements ClassSessionService {

    private final ClassSessionRepository sessionRepository;
    private final ClassSessionMapper sessionMapper;

    @Override
    public ClassSessionResponseDto create(ClassSessionRequestDto request) {
        log.info("Creating class session for date: {}", request.getSessionDate());

        ClassSession session = sessionMapper.toEntity(request);
        session.setStatus(SessionStatus.SCHEDULED);

        ClassSession saved = sessionRepository.save(session);
        log.info("Created session with ID: {}", saved.getId());

        return sessionMapper.toDto(saved);
    }

    @Override
    public ClassSessionResponseDto getById(String id) {
        return sessionMapper.toDto(findById(id));
    }

    @Override
    public ClassSessionResponseDto update(String id, ClassSessionRequestDto request) {
        log.info("Updating session: {}", id);
        ClassSession session = findById(id);
        sessionMapper.updateEntity(request, session);
        return sessionMapper.toDto(sessionRepository.save(session));
    }

    @Override
    public void delete(String id) {
        log.warn("Deleting session: {}", id);
        ClassSession session = findById(id);
        sessionRepository.delete(session);
    }

    @Override
    public Page<ClassSessionResponseDto> getAll(Pageable pageable) {
        return sessionRepository.findAll(pageable).map(sessionMapper::toDto);
    }

    @Override
    public List<ClassSessionResponseDto> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return sessionRepository.findBySessionDateBetween(startDate, endDate)
                .stream().map(sessionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ClassSessionResponseDto> getByDate(LocalDate date) {
        return sessionRepository.findBySessionDate(date)
                .stream().map(sessionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<ClassSessionResponseDto> getUpcoming(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return sessionRepository.findBySessionDateGreaterThanEqualAndStatus(
                today, SessionStatus.SCHEDULED, pageable).map(sessionMapper::toDto);
    }

    @Override
    public ClassSessionResponseDto updateStatus(String id, SessionStatus status, String reason) {
        log.info("Updating session {} status to: {}", id, status);
        ClassSession session = findById(id);
        session.setStatus(status);
        if (status == SessionStatus.CANCELLED || status == SessionStatus.POSTPONED) {
            session.setCancellationReason(reason);
        }
        return sessionMapper.toDto(sessionRepository.save(session));
    }

    @Override
    public ClassSessionResponseDto getSessionWithAttendance(String id) {
        ClassSession session = findById(id);
        return sessionMapper.toDto(session);
    }

    private ClassSession findById(String id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession", "id", id));
    }
}
