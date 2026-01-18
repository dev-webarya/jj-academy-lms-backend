package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.request.GalleryItemRequestDto;
import com.artclassmanagement.dto.response.GalleryItemResponseDto;
import com.artclassmanagement.entity.GalleryItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GalleryItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    @Mapping(target = "uploaderName", ignore = true)
    @Mapping(target = "uploaderRole", ignore = true)
    @Mapping(target = "className", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    @Mapping(target = "verifiedBy", ignore = true)
    @Mapping(target = "verifiedByName", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "isPublic", ignore = true)
    @Mapping(target = "isFeatured", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GalleryItem toEntity(GalleryItemRequestDto dto);

    GalleryItemResponseDto toDto(GalleryItem entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    @Mapping(target = "uploaderName", ignore = true)
    @Mapping(target = "uploaderRole", ignore = true)
    void updateEntity(GalleryItemRequestDto dto, @MappingTarget GalleryItem entity);
}
