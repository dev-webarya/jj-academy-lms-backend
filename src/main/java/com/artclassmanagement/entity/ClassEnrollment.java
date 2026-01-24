package com.artclassmanagement.entity;

import com.artclassmanagement.enums.EnrollmentStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

/**
 * ClassEnrollment - Maps to ArtAcademy's class_enrollments collection.
 * This entity is READ from ArtAcademy and status is updated by LMS admin.
 * Do NOT add additional fields to maintain compatibility with ArtAcademy.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "class_enrollments")
public class ClassEnrollment {

    @Id
    private String id;

    // --- User Link ---
    @Field("user_id")
    private String userId;

    // --- Student Contact Info ---
    @Field("student_name")
    private String studentName;

    @Field("student_email")
    private String studentEmail;

    @Field("student_phone")
    private String studentPhone;

    // --- Class Info ---
    @Field("class_id")
    private String classId;

    @Field("class_name")
    private String className;

    @Field("class_description")
    private String classDescription;

    // --- Enrollment Specifics ---
    @Field("parent_guardian_name")
    private String parentGuardianName;

    @Field("student_age")
    private Integer studentAge;

    @Field("schedule")
    private String schedule;

    @Field("additional_message")
    private String additionalMessage;

    // --- Status ---
    @Field("status")
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    @Field("admin_notes")
    private String adminNotes;

    // --- Timestamps ---
    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;
}
