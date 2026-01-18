package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.request.BatchRequestDto;
import com.artclassmanagement.dto.response.BatchResponseDto;
import com.artclassmanagement.entity.Batch;
import com.artclassmanagement.entity.Instructor;
import com.artclassmanagement.entity.User;
import com.artclassmanagement.enums.BatchStatus;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.mapper.BatchMapper;
import com.artclassmanagement.repository.BatchRepository;
import com.artclassmanagement.repository.InstructorRepository;
import com.artclassmanagement.repository.UserRepository;
import com.artclassmanagement.service.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final BatchMapper batchMapper;

    @Override
    public BatchResponseDto create(BatchRequestDto request) {
        log.info("Creating batch: {}", request.getBatchName());

        Batch batch = batchMapper.toEntity(request);

        // Generate batch code if not provided
        if (request.getBatchCode() == null || request.getBatchCode().isBlank()) {
            batch.setBatchCode(generateBatchCode(request));
        }

        // Set instructor name
        Instructor instructor = instructorRepository.findByUserId(request.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "userId", request.getInstructorId()));
        batch.setInstructorName(instructor.getDisplayName());

        // TODO: Fetch class name from ArtAcademy's artclasses collection
        batch.setClassName("Art Class"); // Placeholder

        batch.setStatus(BatchStatus.UPCOMING);
        batch.setCurrentStudentCount(0);

        return batchMapper.toDto(batchRepository.save(batch));
    }

    @Override
    public BatchResponseDto getById(String id) {
        return batchMapper.toDto(findById(id));
    }

    @Override
    public Page<BatchResponseDto> getAll(Pageable pageable) {
        return batchRepository.findAll(pageable).map(batchMapper::toDto);
    }

    @Override
    public Page<BatchResponseDto> getByClassId(String classId, Pageable pageable) {
        return batchRepository.findByClassId(classId, pageable).map(batchMapper::toDto);
    }

    @Override
    public Page<BatchResponseDto> getByInstructorId(String instructorId, Pageable pageable) {
        return batchRepository.findByInstructorId(instructorId, pageable).map(batchMapper::toDto);
    }

    @Override
    public Page<BatchResponseDto> getByStatus(BatchStatus status, Pageable pageable) {
        return batchRepository.findByStatus(status, pageable).map(batchMapper::toDto);
    }

    @Override
    public BatchResponseDto update(String id, BatchRequestDto request) {
        log.info("Updating batch: {}", id);
        Batch batch = findById(id);

        batchMapper.updateEntity(request, batch);

        // Update instructor name if changed
        if (request.getInstructorId() != null && !request.getInstructorId().equals(batch.getInstructorId())) {
            Instructor instructor = instructorRepository.findByUserId(request.getInstructorId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Instructor", "userId", request.getInstructorId()));
            batch.setInstructorId(request.getInstructorId());
            batch.setInstructorName(instructor.getDisplayName());
        }

        return batchMapper.toDto(batchRepository.save(batch));
    }

    @Override
    public BatchResponseDto updateStatus(String id, BatchStatus status) {
        log.info("Updating batch {} status to: {}", id, status);
        Batch batch = findById(id);
        batch.setStatus(status);
        return batchMapper.toDto(batchRepository.save(batch));
    }

    @Override
    public void delete(String id) {
        log.warn("Deleting batch: {}", id);
        Batch batch = findById(id);
        batchRepository.delete(batch);
    }

    private Batch findById(String id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", id));
    }

    private String generateBatchCode(BatchRequestDto request) {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String timeIndicator = request.getStartTime().getHour() < 12 ? "AM" : "PM";
        return "B-" + datePrefix + "-" + timeIndicator + "-" + System.currentTimeMillis() % 1000;
    }
}
