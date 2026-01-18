package com.artclassmanagement.dto.response;

import com.artclassmanagement.enums.BatchStatus;
import com.artclassmanagement.enums.DayOfWeekEnum;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BatchResponseDto {
    private String id;
    private String classId;
    private String className;
    private String batchName;
    private String batchCode;
    private String instructorId;
    private String instructorName;
    private List<DayOfWeekEnum> classDays;
    private LocalTime startTime;
    private LocalTime endTime;
    private String timezone;
    private Integer maxStudents;
    private Integer currentStudentCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private BatchStatus status;
    private String description;
    private String location;
    private String defaultMeetingLink;
    private Boolean hasAvailableSlots;
    private Instant createdAt;
    private Instant updatedAt;
}
