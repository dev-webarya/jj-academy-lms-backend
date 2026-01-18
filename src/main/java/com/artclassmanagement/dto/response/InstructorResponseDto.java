package com.artclassmanagement.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class InstructorResponseDto {
    private String id;
    private String userId;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String bio;
    private String profileImageUrl;
    private List<String> specializations;
    private List<String> qualifications;
    private Integer yearsOfExperience;
    private String linkedInUrl;
    private String portfolioUrl;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}
