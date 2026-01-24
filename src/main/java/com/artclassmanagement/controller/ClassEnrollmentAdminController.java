package com.artclassmanagement.controller;

import com.artclassmanagement.dto.response.ClassEnrollmentResponseDto;
import com.artclassmanagement.enums.EnrollmentStatus;
import com.artclassmanagement.security.annotations.AdminOnly;
import com.artclassmanagement.service.ClassEnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Admin controller for managing ClassEnrollments from ArtAcademy.
 * Simplified - no batch assignment needed.
 */
@RestController
@RequestMapping("/api/admin/enrollments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Class Enrollments (Admin)", description = "Admin endpoints for managing class enrollments")
public class ClassEnrollmentAdminController {

    private final ClassEnrollmentService classEnrollmentService;

    @GetMapping
    @AdminOnly
    @Operation(summary = "Get all class enrollments (paginated)")
    public ResponseEntity<Page<ClassEnrollmentResponseDto>> getAllEnrollments(Pageable pageable) {
        return ResponseEntity.ok(classEnrollmentService.getAllEnrollments(pageable));
    }

    @GetMapping("/pending")
    @AdminOnly
    @Operation(summary = "Get pending (new) class enrollments")
    public ResponseEntity<Page<ClassEnrollmentResponseDto>> getPendingEnrollments(Pageable pageable) {
        return ResponseEntity.ok(classEnrollmentService.getByStatus(EnrollmentStatus.PENDING, pageable));
    }

    @GetMapping("/count/pending")
    @AdminOnly
    @Operation(summary = "Count pending enrollments")
    public ResponseEntity<Long> countPendingEnrollments() {
        return ResponseEntity.ok(classEnrollmentService.countPendingEnrollments());
    }

    @GetMapping("/status/{status}")
    @AdminOnly
    @Operation(summary = "Get class enrollments by status")
    public ResponseEntity<Page<ClassEnrollmentResponseDto>> getByStatus(
            @PathVariable EnrollmentStatus status, Pageable pageable) {
        return ResponseEntity.ok(classEnrollmentService.getByStatus(status, pageable));
    }

    @GetMapping("/class/{classId}")
    @AdminOnly
    @Operation(summary = "Get enrollments by class ID")
    public ResponseEntity<Page<ClassEnrollmentResponseDto>> getByClassId(
            @PathVariable String classId, Pageable pageable) {
        return ResponseEntity.ok(classEnrollmentService.getByClassId(classId, pageable));
    }

    @GetMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Get single enrollment by ID")
    public ResponseEntity<ClassEnrollmentResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(classEnrollmentService.getById(id));
    }

    @PostMapping("/{id}/approve")
    @AdminOnly
    @Operation(summary = "Approve an enrollment")
    public ResponseEntity<ClassEnrollmentResponseDto> approveEnrollment(
            @PathVariable String id,
            @RequestParam(required = false) String adminNotes) {
        return ResponseEntity.ok(classEnrollmentService.approveEnrollment(id, adminNotes));
    }

    @PostMapping("/{id}/reject")
    @AdminOnly
    @Operation(summary = "Reject an enrollment")
    public ResponseEntity<ClassEnrollmentResponseDto> rejectEnrollment(
            @PathVariable String id,
            @RequestParam(required = false) String adminNotes) {
        return ResponseEntity.ok(classEnrollmentService.rejectEnrollment(id, adminNotes));
    }

    @PatchMapping("/{id}/status")
    @AdminOnly
    @Operation(summary = "Update enrollment status")
    public ResponseEntity<ClassEnrollmentResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam EnrollmentStatus status,
            @RequestParam(required = false) String adminNotes) {
        return ResponseEntity.ok(classEnrollmentService.updateStatus(id, status, adminNotes));
    }
}
