package com.artclassmanagement.entity;

import com.artclassmanagement.enums.MediaType;
import com.artclassmanagement.enums.VerificationStatus;
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
 * GalleryItem - Student/Instructor artwork uploads requiring admin verification
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lms_gallery")
public class GalleryItem {

    @Id
    private String id;

    @Indexed
    private String uploadedBy; // User ID

    private String uploaderName;

    private String uploaderRole; // STUDENT or INSTRUCTOR

    private String title;

    private String description;

    private String mediaUrl;

    private MediaType mediaType;

    private String thumbnailUrl;

    private String classId; // Optional - related class

    private String className;

    private String batchId; // Optional - related batch

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    @Indexed
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    private String verifiedBy; // Admin ID

    private String verifiedByName;

    private Instant verifiedAt;

    private String rejectionReason;

    @Builder.Default
    private Boolean isPublic = false; // Only true after approval

    @Builder.Default
    private Boolean isFeatured = false; // Admin can feature items

    @Builder.Default
    private Integer viewCount = 0;

    @Builder.Default
    private Integer likeCount = 0;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Helper methods
    public void approve(String adminId, String adminName) {
        this.verificationStatus = VerificationStatus.APPROVED;
        this.verifiedBy = adminId;
        this.verifiedByName = adminName;
        this.verifiedAt = Instant.now();
        this.isPublic = true;
    }

    public void reject(String adminId, String adminName, String reason) {
        this.verificationStatus = VerificationStatus.REJECTED;
        this.verifiedBy = adminId;
        this.verifiedByName = adminName;
        this.verifiedAt = Instant.now();
        this.rejectionReason = reason;
        this.isPublic = false;
    }
}
