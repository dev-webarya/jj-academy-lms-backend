package com.artclassmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ClassSessionRequestDto {

    @NotBlank(message = "Batch ID is required")
    private String batchId;

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

    private List<String> materials;
}
