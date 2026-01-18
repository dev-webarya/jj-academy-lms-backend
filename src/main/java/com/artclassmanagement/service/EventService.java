package com.artclassmanagement.service;

import com.artclassmanagement.dto.request.EventRequestDto;
import com.artclassmanagement.dto.response.EventResponseDto;
import com.artclassmanagement.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {
    EventResponseDto create(EventRequestDto request);

    EventResponseDto getById(String id);

    Page<EventResponseDto> getAll(Pageable pageable);

    Page<EventResponseDto> getPublic(Pageable pageable);

    Page<EventResponseDto> getByType(EventType type, Pageable pageable);

    EventResponseDto update(String id, EventRequestDto request);

    EventResponseDto toggleRegistration(String id, Boolean open);

    void delete(String id);
}
