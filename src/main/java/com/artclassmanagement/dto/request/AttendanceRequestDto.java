package com.artclassmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for marking attendance for a session.
 * Admin marks students as present or absent.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequestDto {

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotNull(message = "Attendance records are required")
    private List<StudentAttendance> attendanceRecords;

    /**
     * Individual student attendance record.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentAttendance {
        @NotBlank(message = "Student ID is required")
        private String studentId;

        @NotNull(message = "Present status is required")
        private Boolean isPresent; // Simplified: true = present, false = absent

        private String remarks; // Optional notes
    }
}
