package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.request.GalleryItemRequestDto;
import com.artclassmanagement.dto.request.GalleryVerifyRequestDto;
import com.artclassmanagement.dto.response.GalleryItemResponseDto;
import com.artclassmanagement.entity.GalleryItem;
import com.artclassmanagement.entity.User;
import com.artclassmanagement.enums.VerificationStatus;
import com.artclassmanagement.exception.AccessDeniedException;
import com.artclassmanagement.exception.InvalidRequestException;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.mapper.GalleryItemMapper;
import com.artclassmanagement.repository.GalleryItemRepository;
import com.artclassmanagement.repository.UserRepository;
import com.artclassmanagement.service.GalleryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GalleryServiceImpl implements GalleryService {

    private final GalleryItemRepository galleryRepository;
    private final UserRepository userRepository;
    private final GalleryItemMapper galleryMapper;

    @Override
    public GalleryItemResponseDto upload(GalleryItemRequestDto request) {
        User currentUser = getCurrentUser();
        log.info("User {} uploading gallery item: {}", currentUser.getId(), request.getTitle());

        String role = currentUser.getRoles().contains("ROLE_INSTRUCTOR") ? "INSTRUCTOR"
                : currentUser.getRoles().contains("ROLE_STUDENT") ? "STUDENT" : "OTHER";

        GalleryItem item = galleryMapper.toEntity(request);
        item.setUploadedBy(currentUser.getId());
        item.setUploaderName(currentUser.getFullName());
        item.setUploaderRole(role);
        item.setVerificationStatus(VerificationStatus.PENDING);
        item.setIsPublic(false);
        item.setIsFeatured(false);

        return galleryMapper.toDto(galleryRepository.save(item));
    }

    @Override
    public GalleryItemResponseDto getById(String id) {
        return galleryMapper.toDto(findById(id));
    }

    @Override
    public Page<GalleryItemResponseDto> getPublic(Pageable pageable) {
        return galleryRepository.findByIsPublicTrue(pageable).map(galleryMapper::toDto);
    }

    @Override
    public Page<GalleryItemResponseDto> getPending(Pageable pageable) {
        return galleryRepository.findByVerificationStatus(VerificationStatus.PENDING, pageable)
                .map(galleryMapper::toDto);
    }

    @Override
    public Page<GalleryItemResponseDto> getMyUploads(Pageable pageable) {
        User currentUser = getCurrentUser();
        return galleryRepository.findByUploadedBy(currentUser.getId(), pageable).map(galleryMapper::toDto);
    }

    @Override
    public Page<GalleryItemResponseDto> getFeatured(Pageable pageable) {
        return galleryRepository.findByIsFeaturedTrue(pageable).map(galleryMapper::toDto);
    }

    @Override
    public GalleryItemResponseDto verify(String id, GalleryVerifyRequestDto request) {
        User admin = getCurrentUser();
        log.info("Admin {} verifying gallery item: {}", admin.getId(), id);

        GalleryItem item = findById(id);

        if (request.getApproved()) {
            item.approve(admin.getId(), admin.getFullName());
        } else {
            if (request.getRejectionReason() == null || request.getRejectionReason().isBlank()) {
                throw new InvalidRequestException("Rejection reason is required when rejecting");
            }
            item.reject(admin.getId(), admin.getFullName(), request.getRejectionReason());
        }

        return galleryMapper.toDto(galleryRepository.save(item));
    }

    @Override
    public GalleryItemResponseDto toggleFeatured(String id, Boolean featured) {
        log.info("Setting gallery item {} featured: {}", id, featured);
        GalleryItem item = findById(id);

        if (item.getVerificationStatus() != VerificationStatus.APPROVED) {
            throw new InvalidRequestException("Only approved items can be featured");
        }

        item.setIsFeatured(featured);
        return galleryMapper.toDto(galleryRepository.save(item));
    }

    @Override
    public GalleryItemResponseDto update(String id, GalleryItemRequestDto request) {
        GalleryItem item = findById(id);
        User currentUser = getCurrentUser();

        if (!item.getUploadedBy().equals(currentUser.getId()) &&
                !currentUser.getRoles().contains("ROLE_ADMIN")) {
            throw new AccessDeniedException("You can only update your own uploads");
        }

        galleryMapper.updateEntity(request, item);

        // Reset verification if content changed
        if (item.getVerificationStatus() == VerificationStatus.APPROVED) {
            item.setVerificationStatus(VerificationStatus.PENDING);
            item.setIsPublic(false);
        }

        return galleryMapper.toDto(galleryRepository.save(item));
    }

    @Override
    public void delete(String id) {
        GalleryItem item = findById(id);
        User currentUser = getCurrentUser();

        if (!item.getUploadedBy().equals(currentUser.getId()) &&
                !currentUser.getRoles().contains("ROLE_ADMIN")) {
            throw new AccessDeniedException("You can only delete your own uploads");
        }

        galleryRepository.delete(item);
    }

    private GalleryItem findById(String id) {
        return galleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GalleryItem", "id", id));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
