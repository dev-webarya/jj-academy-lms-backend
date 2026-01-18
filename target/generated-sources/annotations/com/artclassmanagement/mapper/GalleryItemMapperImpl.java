package com.artclassmanagement.mapper;

import com.artclassmanagement.dto.request.GalleryItemRequestDto;
import com.artclassmanagement.dto.response.GalleryItemResponseDto;
import com.artclassmanagement.entity.GalleryItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-18T19:35:18+0530",
    comments = "version: 1.6.2, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class GalleryItemMapperImpl implements GalleryItemMapper {

    @Override
    public GalleryItem toEntity(GalleryItemRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        GalleryItem.GalleryItemBuilder galleryItem = GalleryItem.builder();

        galleryItem.batchId( dto.getBatchId() );
        galleryItem.classId( dto.getClassId() );
        galleryItem.description( dto.getDescription() );
        galleryItem.mediaType( dto.getMediaType() );
        galleryItem.mediaUrl( dto.getMediaUrl() );
        List<String> list = dto.getTags();
        if ( list != null ) {
            galleryItem.tags( new ArrayList<String>( list ) );
        }
        galleryItem.thumbnailUrl( dto.getThumbnailUrl() );
        galleryItem.title( dto.getTitle() );

        return galleryItem.build();
    }

    @Override
    public GalleryItemResponseDto toDto(GalleryItem entity) {
        if ( entity == null ) {
            return null;
        }

        GalleryItemResponseDto galleryItemResponseDto = new GalleryItemResponseDto();

        galleryItemResponseDto.setBatchId( entity.getBatchId() );
        galleryItemResponseDto.setClassId( entity.getClassId() );
        galleryItemResponseDto.setClassName( entity.getClassName() );
        galleryItemResponseDto.setCreatedAt( entity.getCreatedAt() );
        galleryItemResponseDto.setDescription( entity.getDescription() );
        galleryItemResponseDto.setId( entity.getId() );
        galleryItemResponseDto.setIsFeatured( entity.getIsFeatured() );
        galleryItemResponseDto.setIsPublic( entity.getIsPublic() );
        galleryItemResponseDto.setLikeCount( entity.getLikeCount() );
        galleryItemResponseDto.setMediaType( entity.getMediaType() );
        galleryItemResponseDto.setMediaUrl( entity.getMediaUrl() );
        galleryItemResponseDto.setRejectionReason( entity.getRejectionReason() );
        List<String> list = entity.getTags();
        if ( list != null ) {
            galleryItemResponseDto.setTags( new ArrayList<String>( list ) );
        }
        galleryItemResponseDto.setThumbnailUrl( entity.getThumbnailUrl() );
        galleryItemResponseDto.setTitle( entity.getTitle() );
        galleryItemResponseDto.setUpdatedAt( entity.getUpdatedAt() );
        galleryItemResponseDto.setUploadedBy( entity.getUploadedBy() );
        galleryItemResponseDto.setUploaderName( entity.getUploaderName() );
        galleryItemResponseDto.setUploaderRole( entity.getUploaderRole() );
        galleryItemResponseDto.setVerificationStatus( entity.getVerificationStatus() );
        galleryItemResponseDto.setVerifiedAt( entity.getVerifiedAt() );
        galleryItemResponseDto.setVerifiedBy( entity.getVerifiedBy() );
        galleryItemResponseDto.setVerifiedByName( entity.getVerifiedByName() );
        galleryItemResponseDto.setViewCount( entity.getViewCount() );

        return galleryItemResponseDto;
    }

    @Override
    public void updateEntity(GalleryItemRequestDto dto, GalleryItem entity) {
        if ( dto == null ) {
            return;
        }

        entity.setBatchId( dto.getBatchId() );
        entity.setClassId( dto.getClassId() );
        entity.setDescription( dto.getDescription() );
        entity.setMediaType( dto.getMediaType() );
        entity.setMediaUrl( dto.getMediaUrl() );
        if ( entity.getTags() != null ) {
            List<String> list = dto.getTags();
            if ( list != null ) {
                entity.getTags().clear();
                entity.getTags().addAll( list );
            }
            else {
                entity.setTags( null );
            }
        }
        else {
            List<String> list = dto.getTags();
            if ( list != null ) {
                entity.setTags( new ArrayList<String>( list ) );
            }
        }
        entity.setThumbnailUrl( dto.getThumbnailUrl() );
        entity.setTitle( dto.getTitle() );
    }
}
