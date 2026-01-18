package com.artclassmanagement.dto.request;

import com.artclassmanagement.enums.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class EventRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Event type is required")
    private EventType eventType;

    @NotNull(message = "Start date/time is required")
    private Instant startDateTime;

    @NotNull(message = "End date/time is required")
    private Instant endDateTime;

    private String location;

    private Boolean isOnline = false;

    private String meetingLink;

    private String meetingPassword;

    private String imageUrl;

    private String bannerUrl;

    private List<String> instructorIds;

    private Integer maxParticipants;

    private Boolean isPublic = true;

    private Double fee = 0.0;
}
