package com.artclassmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class InstructorRequestDto {

    @NotBlank(message = "User ID is required")
    private String userId;

    private String displayName;

    private String bio;

    private String profileImageUrl;

    private List<String> specializations;

    private List<String> qualifications;

    private Integer yearsOfExperience;

    private String linkedInUrl;

    private String portfolioUrl;
}
