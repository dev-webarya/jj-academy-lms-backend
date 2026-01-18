package com.artclassmanagement.controller;

import com.artclassmanagement.dto.request.GalleryItemRequestDto;
import com.artclassmanagement.dto.request.GalleryVerifyRequestDto;
import com.artclassmanagement.dto.response.GalleryItemResponseDto;
import com.artclassmanagement.service.GalleryService;
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
@RequestMapping("/api/v1/gallery")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Gallery", description = "Gallery management with verification workflow")
public class GalleryController {

    private final GalleryService galleryService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR')")
    @Operation(summary = "Upload gallery item (Student/Instructor)")
    public ResponseEntity<GalleryItemResponseDto> upload(@Valid @RequestBody GalleryItemRequestDto request) {
        return new ResponseEntity<>(galleryService.upload(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get gallery item by ID")
    public ResponseEntity<GalleryItemResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(galleryService.getById(id));
    }

    @GetMapping("/public")
    @Operation(summary = "Get public (approved) gallery items")
    public ResponseEntity<Page<GalleryItemResponseDto>> getPublic(Pageable pageable) {
        return ResponseEntity.ok(galleryService.getPublic(pageable));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get pending gallery items (Admin)")
    public ResponseEntity<Page<GalleryItemResponseDto>> getPending(Pageable pageable) {
        return ResponseEntity.ok(galleryService.getPending(pageable));
    }

    @GetMapping("/my-uploads")
    @Operation(summary = "Get my uploads")
    public ResponseEntity<Page<GalleryItemResponseDto>> getMyUploads(Pageable pageable) {
        return ResponseEntity.ok(galleryService.getMyUploads(pageable));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured gallery items")
    public ResponseEntity<Page<GalleryItemResponseDto>> getFeatured(Pageable pageable) {
        return ResponseEntity.ok(galleryService.getFeatured(pageable));
    }

    @PostMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approve or reject gallery item (Admin)")
    public ResponseEntity<GalleryItemResponseDto> verify(
            @PathVariable String id,
            @RequestBody GalleryVerifyRequestDto request) {
        return ResponseEntity.ok(galleryService.verify(id, request));
    }

    @PatchMapping("/{id}/featured")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle featured status (Admin)")
    public ResponseEntity<GalleryItemResponseDto> toggleFeatured(
            @PathVariable String id,
            @RequestParam Boolean featured) {
        return ResponseEntity.ok(galleryService.toggleFeatured(id, featured));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update gallery item")
    public ResponseEntity<GalleryItemResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody GalleryItemRequestDto request) {
        return ResponseEntity.ok(galleryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete gallery item")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        galleryService.delete(id);
    }
}
