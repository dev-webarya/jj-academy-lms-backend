package com.artclassmanagement.controller;

import com.artclassmanagement.dto.request.EventRequestDto;
import com.artclassmanagement.dto.response.EventResponseDto;
import com.artclassmanagement.enums.EventType;
import com.artclassmanagement.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Events", description = "Event management endpoints")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create event (Admin)")
    public ResponseEntity<EventResponseDto> create(@Valid @RequestBody EventRequestDto request) {
        return new ResponseEntity<>(eventService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<EventResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all events (Admin)")
    public ResponseEntity<Page<EventResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(eventService.getAll(pageable));
    }

    @GetMapping("/public")
    @Operation(summary = "Get public events")
    public ResponseEntity<Page<EventResponseDto>> getPublic(Pageable pageable) {
        return ResponseEntity.ok(eventService.getPublic(pageable));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get events by type")
    public ResponseEntity<Page<EventResponseDto>> getByType(
            @PathVariable EventType type, Pageable pageable) {
        return ResponseEntity.ok(eventService.getByType(type, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update event (Admin)")
    public ResponseEntity<EventResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody EventRequestDto request) {
        return ResponseEntity.ok(eventService.update(id, request));
    }

    @PatchMapping("/{id}/registration")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle event registration (Admin)")
    public ResponseEntity<EventResponseDto> toggleRegistration(
            @PathVariable String id,
            @RequestParam Boolean open) {
        return ResponseEntity.ok(eventService.toggleRegistration(id, open));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete event (Admin)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        eventService.delete(id);
    }
}
