package com.artclassmanagement.dto.request;

import com.artclassmanagement.enums.DayOfWeekEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BatchRequestDto {

    @NotBlank(message = "Class ID is required")
    private String classId;

    @NotBlank(message = "Batch name is required")
    private String batchName;

    private String batchCode;

    @NotBlank(message = "Instructor ID is required")
    private String instructorId;

    @NotNull(message = "Class days are required")
    private List<DayOfWeekEnum> classDays;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    private String timezone = "Asia/Kolkata";

    @NotNull(message = "Maximum students is required")
    @Positive(message = "Maximum students must be positive")
    private Integer maxStudents;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String description;

    private String location;

    private String defaultMeetingLink;
}
