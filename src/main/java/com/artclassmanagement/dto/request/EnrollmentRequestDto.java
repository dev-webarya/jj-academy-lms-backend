package com.artclassmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnrollmentRequestDto {

    @NotBlank(message = "Class ID is required")
    private String classId;

    private String notes;
}
