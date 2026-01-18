package com.artclassmanagement.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Instructor profile - additional metadata for users with INSTRUCTOR role
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_instructors")
public class Instructor {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId; // Reference to User entity

    private String displayName;

    private String email; // Denormalized from User

    private String phoneNumber;

    private String bio;

    private String profileImageUrl;

    @Builder.Default
    private List<String> specializations = new ArrayList<>(); // e.g., ["Oil Painting", "Watercolor"]

    @Builder.Default
    private List<String> qualifications = new ArrayList<>();

    private Integer yearsOfExperience;

    private String linkedInUrl;

    private String portfolioUrl;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean isDeleted = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
