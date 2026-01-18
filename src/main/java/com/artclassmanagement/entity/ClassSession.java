package com.artclassmanagement.entity;

import com.artclassmanagement.enums.SessionStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassSession - Individual scheduled class session by instructor
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_class_sessions")
@CompoundIndex(name = "batch_date_idx", def = "{'batchId': 1, 'sessionDate': 1}")
public class ClassSession {

    @Id
    private String id;

    @Indexed
    private String batchId;

    private String batchName; // Denormalized

    @Indexed
    private String instructorId;

    private String instructorName; // Denormalized

    private LocalDate sessionDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String topic;

    private String description;

    private String meetingLink; // Zoom/Google Meet link

    private String meetingPassword;

    @Builder.Default
    private List<String> materials = new ArrayList<>(); // Links to resources/documents

    @Builder.Default
    private SessionStatus status = SessionStatus.SCHEDULED;

    private String cancellationReason;

    private LocalDate rescheduledTo; // If postponed

    // Attendance summary (updated after attendance is taken)
    @Builder.Default
    private Integer totalStudents = 0;

    @Builder.Default
    private Integer presentCount = 0;

    @Builder.Default
    private Integer absentCount = 0;

    @Builder.Default
    private Boolean attendanceTaken = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Helper methods
    public void updateAttendanceSummary(int present, int absent, int total) {
        this.presentCount = present;
        this.absentCount = absent;
        this.totalStudents = total;
        this.attendanceTaken = true;
    }
}
