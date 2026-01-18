package com.artclassmanagement.entity;

import com.artclassmanagement.enums.AttendanceStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Attendance - Per-student per-session attendance record
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_attendance")
@CompoundIndex(name = "session_student_idx", def = "{'sessionId': 1, 'studentId': 1}", unique = true)
public class Attendance {

    @Id
    private String id;

    @Indexed
    private String sessionId;

    @Indexed
    private String batchId;

    @Indexed
    private String studentId;

    private String studentName; // Denormalized

    private String studentEmail; // Denormalized

    @Builder.Default
    private AttendanceStatus status = AttendanceStatus.ABSENT;

    private Instant markedAt;

    private String markedBy; // Instructor ID who marked

    private String markedByName; // Instructor name

    private String remarks; // Optional notes (e.g., "Late by 15 minutes")

    private Integer lateByMinutes; // If status is LATE

    @CreatedDate
    private Instant createdAt;

    // Helper
    public void markPresent(String instructorId, String instructorName) {
        this.status = AttendanceStatus.PRESENT;
        this.markedAt = Instant.now();
        this.markedBy = instructorId;
        this.markedByName = instructorName;
    }

    public void markAbsent(String instructorId, String instructorName) {
        this.status = AttendanceStatus.ABSENT;
        this.markedAt = Instant.now();
        this.markedBy = instructorId;
        this.markedByName = instructorName;
    }

    public void markLate(String instructorId, String instructorName, int lateMinutes) {
        this.status = AttendanceStatus.LATE;
        this.lateByMinutes = lateMinutes;
        this.markedAt = Instant.now();
        this.markedBy = instructorId;
        this.markedByName = instructorName;
    }
}
