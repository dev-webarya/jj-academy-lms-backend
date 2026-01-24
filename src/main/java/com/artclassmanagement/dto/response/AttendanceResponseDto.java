package com.artclassmanagement.dto.response;

import com.artclassmanagement.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Response DTO for attendance records with over-limit tracking.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponseDto {
    private String id;
    private String sessionId;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String subscriptionId;

    private LocalDate sessionDate;
    private Integer sessionMonth;
    private Integer sessionYear;

    // Monthly session tracking
    private Integer sessionCountThisMonth;
    private Boolean isOverLimit;

    private AttendanceStatus status;
    private Instant markedAt;
    private String remarks;
    private Instant createdAt;
}
