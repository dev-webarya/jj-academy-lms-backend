package com.artclassmanagement.controller;

import com.artclassmanagement.dto.request.ClassSessionRequestDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import com.artclassmanagement.enums.SessionStatus;
import com.artclassmanagement.security.annotations.AdminOnly;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for class session management.
 * All write operations require ADMIN role.
 */
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Class Sessions", description = "Class session management (Admin)")
public class ClassSessionController {

    private final ClassSessionService sessionService;

    @PostMapping
    @AdminOnly
    @Operation(summary = "Create a class session")
    public ResponseEntity<ClassSessionResponseDto> create(@Valid @RequestBody ClassSessionRequestDto request) {
        return new ResponseEntity<>(sessionService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get session by ID")
    public ResponseEntity<ClassSessionResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(sessionService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all sessions (paginated)")
    public ResponseEntity<Page<ClassSessionResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(sessionService.getAll(pageable));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get sessions by date")
    public ResponseEntity<List<ClassSessionResponseDto>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(sessionService.getByDate(date));
    }

    @GetMapping("/range")
    @Operation(summary = "Get sessions by date range")
    public ResponseEntity<List<ClassSessionResponseDto>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(sessionService.getByDateRange(startDate, endDate));
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming sessions (paginated)")
    public ResponseEntity<Page<ClassSessionResponseDto>> getUpcoming(Pageable pageable) {
        return ResponseEntity.ok(sessionService.getUpcoming(pageable));
    }

    @GetMapping("/{id}/attendance")
    @AdminOnly
    @Operation(summary = "Get session with attendance records")
    public ResponseEntity<ClassSessionResponseDto> getSessionWithAttendance(@PathVariable String id) {
        return ResponseEntity.ok(sessionService.getSessionWithAttendance(id));
    }

    @PutMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Update session")
    public ResponseEntity<ClassSessionResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody ClassSessionRequestDto request) {
        return ResponseEntity.ok(sessionService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @AdminOnly
    @Operation(summary = "Update session status")
    public ResponseEntity<ClassSessionResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam SessionStatus status,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(sessionService.updateStatus(id, status, reason));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Delete session")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        sessionService.delete(id);
    }
}
