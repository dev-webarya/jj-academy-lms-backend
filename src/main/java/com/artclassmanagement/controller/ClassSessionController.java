package com.artclassmanagement.controller;

import com.artclassmanagement.dto.request.ClassSessionRequestDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import com.artclassmanagement.enums.SessionStatus;
import com.artclassmanagement.service.ClassSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Class Sessions", description = "Class session scheduling endpoints")
public class ClassSessionController {

    private final ClassSessionService sessionService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Create a class session (Instructor)")
    public ResponseEntity<ClassSessionResponseDto> create(@Valid @RequestBody ClassSessionRequestDto request) {
        return new ResponseEntity<>(sessionService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get session by ID")
    public ResponseEntity<ClassSessionResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(sessionService.getById(id));
    }

    @GetMapping("/batch/{batchId}")
    @Operation(summary = "Get sessions by batch ID")
    public ResponseEntity<Page<ClassSessionResponseDto>> getByBatchId(
            @PathVariable String batchId, Pageable pageable) {
        return ResponseEntity.ok(sessionService.getByBatchId(batchId, pageable));
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(summary = "Get sessions by instructor ID")
    public ResponseEntity<Page<ClassSessionResponseDto>> getByInstructorId(
            @PathVariable String instructorId, Pageable pageable) {
        return ResponseEntity.ok(sessionService.getByInstructorId(instructorId, pageable));
    }

    @GetMapping("/batch/{batchId}/range")
    @Operation(summary = "Get sessions by batch and date range")
    public ResponseEntity<List<ClassSessionResponseDto>> getByBatchIdAndDateRange(
            @PathVariable String batchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(sessionService.getByBatchIdAndDateRange(batchId, startDate, endDate));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update session")
    public ResponseEntity<ClassSessionResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody ClassSessionRequestDto request) {
        return ResponseEntity.ok(sessionService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update session status")
    public ResponseEntity<ClassSessionResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam SessionStatus status,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(sessionService.updateStatus(id, status, reason));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Delete session")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        sessionService.delete(id);
    }
}
