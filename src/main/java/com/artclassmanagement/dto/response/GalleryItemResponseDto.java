package com.artclassmanagement.dto.response;

import com.artclassmanagement.enums.MediaType;
import com.artclassmanagement.enums.VerificationStatus;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class GalleryItemResponseDto {
    private String id;
    private String uploadedBy;
    private String uploaderName;
    private String uploaderRole;
    private String title;
    private String description;
    private String mediaUrl;
    private MediaType mediaType;
    private String thumbnailUrl;
    private String classId;
    private String className;
    private String batchId;
    private List<String> tags;
    private VerificationStatus verificationStatus;
    private String verifiedBy;
    private String verifiedByName;
    private Instant verifiedAt;
    private String rejectionReason;
    private Boolean isPublic;
    private Boolean isFeatured;
    private Integer viewCount;
    private Integer likeCount;
    private Instant createdAt;
    private Instant updatedAt;
}
