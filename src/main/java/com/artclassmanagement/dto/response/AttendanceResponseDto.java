package com.artclassmanagement.dto.response;

import com.artclassmanagement.enums.AttendanceStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class AttendanceResponseDto {
    private String id;
    private String sessionId;
    private String batchId;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private AttendanceStatus status;
    private Instant markedAt;
    private String markedBy;
    private String markedByName;
    private String remarks;
    private Integer lateByMinutes;
    private Instant createdAt;
}
