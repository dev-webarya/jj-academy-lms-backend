package com.artclassmanagement.controller;

import com.artclassmanagement.dto.request.EnrollmentRequestDto;
import com.artclassmanagement.dto.response.EnrollmentResponseDto;
import com.artclassmanagement.enums.EnrollmentStatus;
import com.artclassmanagement.security.annotations.AdminOnly;
import com.artclassmanagement.service.EnrollmentService;
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

/**
 * Controller for managing LMS Enrollments.
 * Simplified - no batch assignment needed.
 */
@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Enrollments", description = "Enrollment management endpoints")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Enroll in a class (Student)")
    public ResponseEntity<EnrollmentResponseDto> enroll(@Valid @RequestBody EnrollmentRequestDto request) {
        return new ResponseEntity<>(enrollmentService.enroll(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID")
    public ResponseEntity<EnrollmentResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(enrollmentService.getById(id));
    }

    @GetMapping
    @AdminOnly
    @Operation(summary = "Get all enrollments (Admin)")
    public ResponseEntity<Page<EnrollmentResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(enrollmentService.getAll(pageable));
    }

    @GetMapping("/my-enrollments")
    @Operation(summary = "Get my enrollments")
    public ResponseEntity<Page<EnrollmentResponseDto>> getMyEnrollments(Pageable pageable) {
        return ResponseEntity.ok(enrollmentService.getMyEnrollments(pageable));
    }

    @GetMapping("/class/{classId}")
    @AdminOnly
    @Operation(summary = "Get enrollments by class")
    public ResponseEntity<Page<EnrollmentResponseDto>> getByClassId(
            @PathVariable String classId, Pageable pageable) {
        return ResponseEntity.ok(enrollmentService.getByClassId(classId, pageable));
    }

    @GetMapping("/status/{status}")
    @AdminOnly
    @Operation(summary = "Get enrollments by status (Admin)")
    public ResponseEntity<Page<EnrollmentResponseDto>> getByStatus(
            @PathVariable EnrollmentStatus status, Pageable pageable) {
        return ResponseEntity.ok(enrollmentService.getByStatus(status, pageable));
    }

    @PatchMapping("/{id}/status")
    @AdminOnly
    @Operation(summary = "Update enrollment status (Admin)")
    public ResponseEntity<EnrollmentResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam EnrollmentStatus status,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(enrollmentService.updateStatus(id, status, notes));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Delete enrollment (Admin)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        enrollmentService.delete(id);
    }
}
