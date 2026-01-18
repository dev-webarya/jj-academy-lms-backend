package com.artclassmanagement.controller;

import com.artclassmanagement.dto.request.InstructorRequestDto;
import com.artclassmanagement.dto.response.InstructorResponseDto;
import com.artclassmanagement.service.InstructorService;
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
@RequestMapping("/api/v1/instructors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Instructors", description = "Instructor management endpoints")
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create instructor profile (Admin only)")
    public ResponseEntity<InstructorResponseDto> create(@Valid @RequestBody InstructorRequestDto request) {
        return new ResponseEntity<>(instructorService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get instructor by ID")
    public ResponseEntity<InstructorResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(instructorService.getById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get instructor by user ID")
    public ResponseEntity<InstructorResponseDto> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(instructorService.getByUserId(userId));
    }

    @GetMapping
    @Operation(summary = "Get all instructors")
    public ResponseEntity<Page<InstructorResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(instructorService.getAll(pageable));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active instructors")
    public ResponseEntity<Page<InstructorResponseDto>> getActive(Pageable pageable) {
        return ResponseEntity.ok(instructorService.getActive(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @instructorSecurityService.isOwner(#id)")
    @Operation(summary = "Update instructor")
    public ResponseEntity<InstructorResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody InstructorRequestDto request) {
        return ResponseEntity.ok(instructorService.update(id, request));
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle instructor active status (Admin only)")
    public ResponseEntity<InstructorResponseDto> toggleActive(
            @PathVariable String id,
            @RequestParam Boolean isActive) {
        return ResponseEntity.ok(instructorService.toggleActive(id, isActive));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete instructor (Admin only)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        instructorService.delete(id);
    }
}
