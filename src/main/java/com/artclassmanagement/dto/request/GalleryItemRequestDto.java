package com.artclassmanagement.dto.request;

import com.artclassmanagement.enums.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GalleryItemRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Media URL is required")
    private String mediaUrl;

    @NotNull(message = "Media type is required")
    private MediaType mediaType;

    private String thumbnailUrl;

    private String classId;

    private String batchId;

    private List<String> tags;
}
