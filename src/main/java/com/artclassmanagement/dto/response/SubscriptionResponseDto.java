package com.artclassmanagement.dto.response;

import com.artclassmanagement.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDto {

    private String id;

    private String studentId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;

    private Integer subscriptionMonth;
    private Integer subscriptionYear;
    private LocalDate startDate;
    private LocalDate endDate;

    private Integer allowedSessions;
    private Integer attendedSessions;
    private Integer remainingSessions;
    private Boolean isOverLimit;

    private SubscriptionStatus status;

    private String paymentId;
    private Double amountPaid;
    private String notes;

    private Instant createdAt;
    private Instant updatedAt;
}
