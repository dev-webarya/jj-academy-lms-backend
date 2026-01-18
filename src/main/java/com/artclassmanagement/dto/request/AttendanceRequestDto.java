package com.artclassmanagement.dto.request;

import com.artclassmanagement.enums.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AttendanceRequestDto {

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotNull(message = "Attendance records are required")
    private List<StudentAttendance> attendanceRecords;

    @Data
    public static class StudentAttendance {
        @NotBlank(message = "Student ID is required")
        private String studentId;

        @NotNull(message = "Status is required")
        private AttendanceStatus status;

        private String remarks;

        private Integer lateByMinutes; // If status is LATE
    }
}
