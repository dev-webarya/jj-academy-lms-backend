package com.artclassmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignBatchRequestDto {

    @NotBlank(message = "Batch ID is required")
    private String batchId;
}
