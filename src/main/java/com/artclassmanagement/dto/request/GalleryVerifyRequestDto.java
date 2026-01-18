package com.artclassmanagement.dto.request;

import lombok.Data;

@Data
public class GalleryVerifyRequestDto {
    private Boolean approved; // true = approve, false = reject
    private String rejectionReason; // Required if approved = false
}
