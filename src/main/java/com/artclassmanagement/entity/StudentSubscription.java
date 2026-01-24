package com.artclassmanagement.entity;

import com.artclassmanagement.enums.SubscriptionStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

/**
 * StudentSubscription - Monthly subscription for a student.
 * Students can attend up to 8 sessions per month (configurable).
 * No carry-over to next month.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_subscriptions")
@CompoundIndex(name = "student_month_year_idx", def = "{'studentId': 1, 'subscriptionMonth': 1, 'subscriptionYear': 1}", unique = true)
public class StudentSubscription {

    @Id
    private String id;

    @Indexed
    private String studentId; // Reference to User

    private String studentName; // Denormalized
    private String studentEmail; // Denormalized
    private String studentPhone; // Denormalized

    // Subscription period
    private Integer subscriptionMonth; // 1-12
    private Integer subscriptionYear; // e.g., 2026
    private LocalDate startDate; // First day of the month
    private LocalDate endDate; // Last day of the month

    // Session limits
    @Builder.Default
    private Integer allowedSessions = 8; // Configurable limit per month

    @Builder.Default
    private Integer attendedSessions = 0; // Updated when attendance is marked

    @Builder.Default
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    private String paymentId; // Optional payment reference
    private Double amountPaid; // Payment amount
    private String notes; // Admin notes

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Helper methods
    public boolean hasSessionsRemaining() {
        return attendedSessions < allowedSessions;
    }

    public boolean isOverLimit() {
        return attendedSessions > allowedSessions;
    }

    public void incrementAttendedSessions() {
        this.attendedSessions++;
    }

    public void decrementAttendedSessions() {
        if (this.attendedSessions > 0) {
            this.attendedSessions--;
        }
    }

    public int getRemainingSessionCount() {
        return Math.max(0, allowedSessions - attendedSessions);
    }
}
