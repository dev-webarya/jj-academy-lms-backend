package com.artclassmanagement.controller;

import com.artclassmanagement.dto.request.AttendanceRequestDto;
import com.artclassmanagement.dto.response.AttendanceResponseDto;
import com.artclassmanagement.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Attendance", description = "Attendance management endpoints")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Mark attendance for a session (Instructor)")
    public ResponseEntity<List<AttendanceResponseDto>> markAttendance(
            @Valid @RequestBody AttendanceRequestDto request) {
        return ResponseEntity.ok(attendanceService.markAttendance(request));
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get attendance by session")
    public ResponseEntity<List<AttendanceResponseDto>> getBySessionId(@PathVariable String sessionId) {
        return ResponseEntity.ok(attendanceService.getBySessionId(sessionId));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @Operation(summary = "Get attendance by student")
    public ResponseEntity<Page<AttendanceResponseDto>> getByStudentId(
            @PathVariable String studentId, Pageable pageable) {
        return ResponseEntity.ok(attendanceService.getByStudentId(studentId, pageable));
    }

    @GetMapping("/my-attendance")
    @Operation(summary = "Get my attendance (Student)")
    public ResponseEntity<Page<AttendanceResponseDto>> getMyAttendance(Pageable pageable) {
        return ResponseEntity.ok(attendanceService.getMyAttendance(pageable));
    }

    @GetMapping("/student/{studentId}/batch/{batchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @Operation(summary = "Get student attendance for a batch")
    public ResponseEntity<List<AttendanceResponseDto>> getStudentBatchAttendance(
            @PathVariable String studentId,
            @PathVariable String batchId) {
        return ResponseEntity.ok(attendanceService.getStudentBatchAttendance(studentId, batchId));
    }
}
