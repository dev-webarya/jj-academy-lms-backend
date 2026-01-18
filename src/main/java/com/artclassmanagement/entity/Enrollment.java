package com.artclassmanagement.entity;

import com.artclassmanagement.enums.EnrollmentStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Enrollment - Student enrollment in a class, assigned to batch by admin
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_enrollments")
@CompoundIndex(name = "user_class_idx", def = "{'userId': 1, 'classId': 1}", unique = true)
public class Enrollment {

    @Id
    private String id;

    @Indexed
    private String userId; // Student

    private String studentName; // Denormalized

    private String studentEmail; // Denormalized

    @Indexed
    private String classId; // Reference to ArtClass

    private String className; // Denormalized

    @Indexed
    private String batchId; // Assigned by admin, nullable initially

    private String batchName; // Denormalized

    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    private Instant enrollmentDate;

    private Instant assignedToBatchDate;

    private String paymentId; // If enrollment requires payment

    private String notes; // Admin notes

    // Progress tracking
    @Builder.Default
    private Integer sessionsAttended = 0;

    @Builder.Default
    private Integer totalSessions = 0;

    private Double attendancePercentage;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Helper methods
    public void assignToBatch(String batchId, String batchName) {
        this.batchId = batchId;
        this.batchName = batchName;
        this.status = EnrollmentStatus.ASSIGNED;
        this.assignedToBatchDate = Instant.now();
    }

    public void updateAttendanceStats(int attended, int total) {
        this.sessionsAttended = attended;
        this.totalSessions = total;
        this.attendancePercentage = total > 0 ? (attended * 100.0 / total) : 0.0;
    }
}
