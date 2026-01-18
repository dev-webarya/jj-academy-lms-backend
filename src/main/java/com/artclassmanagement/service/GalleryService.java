package com.artclassmanagement.service;

import com.artclassmanagement.dto.request.GalleryItemRequestDto;
import com.artclassmanagement.dto.request.GalleryVerifyRequestDto;
import com.artclassmanagement.dto.response.GalleryItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GalleryService {
    GalleryItemResponseDto upload(GalleryItemRequestDto request);

    GalleryItemResponseDto getById(String id);

    Page<GalleryItemResponseDto> getPublic(Pageable pageable);

    Page<GalleryItemResponseDto> getPending(Pageable pageable); // Admin

    Page<GalleryItemResponseDto> getMyUploads(Pageable pageable);

    Page<GalleryItemResponseDto> getFeatured(Pageable pageable);

    GalleryItemResponseDto verify(String id, GalleryVerifyRequestDto request); // Admin

    GalleryItemResponseDto toggleFeatured(String id, Boolean featured); // Admin

    GalleryItemResponseDto update(String id, GalleryItemRequestDto request);

    void delete(String id);
}
