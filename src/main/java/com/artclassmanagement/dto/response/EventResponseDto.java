package com.artclassmanagement.dto.response;

import com.artclassmanagement.enums.EventType;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class EventResponseDto {
    private String id;
    private String title;
    private String description;
    private EventType eventType;
    private Instant startDateTime;
    private Instant endDateTime;
    private String location;
    private Boolean isOnline;
    private String meetingLink;
    private String meetingPassword;
    private String imageUrl;
    private String bannerUrl;
    private List<String> instructorIds;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private Boolean isPublic;
    private Boolean isRegistrationOpen;
    private Double fee;
    private Boolean hasAvailableSlots;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}
