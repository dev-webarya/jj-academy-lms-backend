package com.artclassmanagement.controller;

import com.artclassmanagement.dto.request.AttendanceRequestDto;
import com.artclassmanagement.dto.response.AttendanceResponseDto;
import com.artclassmanagement.dto.response.ClassSessionResponseDto;
import com.artclassmanagement.security.annotations.AdminOnly;
import com.artclassmanagement.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for attendance management.
 * Admin marks attendance and tracks over-limit students.
 */
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Attendance", description = "Attendance management (Admin)")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @AdminOnly
    @Operation(summary = "Mark attendance for a session (returns session with attendance records)")
    public ResponseEntity<ClassSessionResponseDto> markAttendance(
            @Valid @RequestBody AttendanceRequestDto request) {
        return ResponseEntity.ok(attendanceService.markAttendance(request));
    }

    @GetMapping("/session/{sessionId}")
    @AdminOnly
    @Operation(summary = "Get all attendance records for a session")
    public ResponseEntity<List<AttendanceResponseDto>> getBySessionId(@PathVariable String sessionId) {
        return ResponseEntity.ok(attendanceService.getBySessionId(sessionId));
    }

    @GetMapping("/student/{studentId}")
    @AdminOnly
    @Operation(summary = "Get attendance history for a student")
    public ResponseEntity<Page<AttendanceResponseDto>> getByStudentId(
            @PathVariable String studentId, Pageable pageable) {
        return ResponseEntity.ok(attendanceService.getByStudentId(studentId, pageable));
    }

    @GetMapping("/my-attendance")
    @Operation(summary = "Get my attendance (Student)")
    public ResponseEntity<Page<AttendanceResponseDto>> getMyAttendance(Pageable pageable) {
        return ResponseEntity.ok(attendanceService.getMyAttendance(pageable));
    }

    @GetMapping("/student/{studentId}/monthly")
    @AdminOnly
    @Operation(summary = "Get student's attendance summary for a specific month")
    public ResponseEntity<AttendanceResponseDto> getStudentMonthlyAttendance(
            @PathVariable String studentId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        AttendanceResponseDto result = attendanceService.getStudentMonthlyAttendance(studentId, month, year);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/over-limit")
    @AdminOnly
    @Operation(summary = "Get students who exceeded session limit for a month")
    public ResponseEntity<List<AttendanceResponseDto>> getOverLimitStudents(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        // Default to current month/year if not provided
        LocalDate now = LocalDate.now();
        int m = month != null ? month : now.getMonthValue();
        int y = year != null ? year : now.getYear();
        return ResponseEntity.ok(attendanceService.getOverLimitStudents(m, y));
    }
}
