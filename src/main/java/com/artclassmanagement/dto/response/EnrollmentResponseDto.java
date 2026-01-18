package com.artclassmanagement.dto.response;

import com.artclassmanagement.enums.EnrollmentStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class EnrollmentResponseDto {
    private String id;
    private String userId;
    private String studentName;
    private String studentEmail;
    private String classId;
    private String className;
    private String batchId;
    private String batchName;
    private EnrollmentStatus status;
    private Instant enrollmentDate;
    private Instant assignedToBatchDate;
    private String paymentId;
    private String notes;
    private Integer sessionsAttended;
    private Integer totalSessions;
    private Double attendancePercentage;
    private Instant createdAt;
    private Instant updatedAt;
}
