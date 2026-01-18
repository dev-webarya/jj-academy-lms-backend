package com.artclassmanagement.entity;

import com.artclassmanagement.enums.BatchStatus;
import com.artclassmanagement.enums.DayOfWeekEnum;
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
 * Batch entity - represents a group of students for a specific class
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_batches")
@CompoundIndex(name = "class_status_idx", def = "{'classId': 1, 'status': 1}")
public class Batch {

    @Id
    private String id;

    @Indexed
    private String classId; // Reference to ArtClass from ArtAcademy

    private String className; // Denormalized for display

    private String batchName; // e.g., "Batch-2026-Jan-Morning"

    private String batchCode; // e.g., "WC-2026-01-AM"

    @Indexed
    private String instructorId; // Reference to User with INSTRUCTOR role

    private String instructorName; // Denormalized for display

    // Schedule
    @Builder.Default
    private List<DayOfWeekEnum> classDays = new ArrayList<>(); // e.g., [MONDAY, WEDNESDAY, FRIDAY]

    private LocalTime startTime;

    private LocalTime endTime;

    private String timezone; // e.g., "Asia/Kolkata"

    private Integer maxStudents;

    @Builder.Default
    private Integer currentStudentCount = 0;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private BatchStatus status = BatchStatus.UPCOMING;

    private String description;

    private String location; // Physical location or "Online"

    private String defaultMeetingLink; // Default meeting link for online classes

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Helper methods
    public boolean hasAvailableSlots() {
        return currentStudentCount < maxStudents;
    }

    public void incrementStudentCount() {
        this.currentStudentCount++;
    }

    public void decrementStudentCount() {
        if (this.currentStudentCount > 0) {
            this.currentStudentCount--;
        }
    }
}
