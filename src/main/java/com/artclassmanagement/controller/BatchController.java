package com.artclassmanagement.controller;

import com.artclassmanagement.dto.request.BatchRequestDto;
import com.artclassmanagement.dto.response.BatchResponseDto;
import com.artclassmanagement.enums.BatchStatus;
import com.artclassmanagement.service.BatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/batches")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Batches", description = "Batch management endpoints")
public class BatchController {

    private final BatchService batchService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new batch (Admin only)")
    public ResponseEntity<BatchResponseDto> create(@Valid @RequestBody BatchRequestDto request) {
        log.info("Creating batch: {}", request.getBatchName());
        return new ResponseEntity<>(batchService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get batch by ID")
    public ResponseEntity<BatchResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(batchService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all batches")
    public ResponseEntity<Page<BatchResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(batchService.getAll(pageable));
    }

    @GetMapping("/class/{classId}")
    @Operation(summary = "Get batches by class ID")
    public ResponseEntity<Page<BatchResponseDto>> getByClassId(
            @PathVariable String classId, Pageable pageable) {
        return ResponseEntity.ok(batchService.getByClassId(classId, pageable));
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(summary = "Get batches by instructor ID")
    public ResponseEntity<Page<BatchResponseDto>> getByInstructorId(
            @PathVariable String instructorId, Pageable pageable) {
        return ResponseEntity.ok(batchService.getByInstructorId(instructorId, pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get batches by status")
    public ResponseEntity<Page<BatchResponseDto>> getByStatus(
            @PathVariable BatchStatus status, Pageable pageable) {
        return ResponseEntity.ok(batchService.getByStatus(status, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update batch (Admin only)")
    public ResponseEntity<BatchResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody BatchRequestDto request) {
        log.info("Updating batch: {}", id);
        return ResponseEntity.ok(batchService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update batch status (Admin only)")
    public ResponseEntity<BatchResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam BatchStatus status) {
        log.info("Updating batch {} status to: {}", id, status);
        return ResponseEntity.ok(batchService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete batch (Admin only)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        log.warn("Deleting batch: {}", id);
        batchService.delete(id);
    }
}
