package com.artclassmanagement.dto.response;

import com.artclassmanagement.enums.SessionStatus;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ClassSessionResponseDto {
    private String id;
    private String batchId;
    private String batchName;
    private String instructorId;
    private String instructorName;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String topic;
    private String description;
    private String meetingLink;
    private String meetingPassword;
    private List<String> materials;
    private SessionStatus status;
    private String cancellationReason;
    private LocalDate rescheduledTo;
    private Integer totalStudents;
    private Integer presentCount;
    private Integer absentCount;
    private Boolean attendanceTaken;
    private Instant createdAt;
    private Instant updatedAt;
}
