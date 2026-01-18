package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.request.EventRequestDto;
import com.artclassmanagement.dto.response.EventResponseDto;
import com.artclassmanagement.entity.Event;
import com.artclassmanagement.entity.User;
import com.artclassmanagement.enums.EventType;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.mapper.EventMapper;
import com.artclassmanagement.repository.EventRepository;
import com.artclassmanagement.repository.UserRepository;
import com.artclassmanagement.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    @Override
    public EventResponseDto create(EventRequestDto request) {
        User admin = getCurrentUser();
        log.info("Admin {} creating event: {}", admin.getId(), request.getTitle());

        Event event = eventMapper.toEntity(request);
        event.setCreatedBy(admin.getId());
        event.setIsRegistrationOpen(true);
        event.setCurrentParticipants(0);
        event.setIsDeleted(false);

        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public EventResponseDto getById(String id) {
        return eventMapper.toDto(findById(id));
    }

    @Override
    public Page<EventResponseDto> getAll(Pageable pageable) {
        return eventRepository.findByIsDeletedFalse(pageable).map(eventMapper::toDto);
    }

    @Override
    public Page<EventResponseDto> getPublic(Pageable pageable) {
        return eventRepository.findByIsPublicTrueAndIsDeletedFalse(pageable).map(eventMapper::toDto);
    }

    @Override
    public Page<EventResponseDto> getByType(EventType type, Pageable pageable) {
        return eventRepository.findByEventTypeAndIsDeletedFalse(type, pageable).map(eventMapper::toDto);
    }

    @Override
    public EventResponseDto update(String id, EventRequestDto request) {
        log.info("Updating event: {}", id);
        Event event = findById(id);
        eventMapper.updateEntity(request, event);
        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public EventResponseDto toggleRegistration(String id, Boolean open) {
        log.info("Setting event {} registration to: {}", id, open);
        Event event = findById(id);
        event.setIsRegistrationOpen(open);
        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public void delete(String id) {
        log.warn("Soft deleting event: {}", id);
        Event event = findById(id);
        event.setIsDeleted(true);
        eventRepository.save(event);
    }

    private Event findById(String id) {
        return eventRepository.findById(id)
                .filter(e -> !e.getIsDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
