package com.artclassmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request DTO for creating/updating a class session.
 * Simplified - no batch/instructor references.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassSessionRequestDto {

    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotBlank(message = "Topic is required")
    private String topic;

    private String description;

    private String meetingLink;

    private String meetingPassword;
}
