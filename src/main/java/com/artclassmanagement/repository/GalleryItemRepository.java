package com.artclassmanagement.repository;

import com.artclassmanagement.entity.GalleryItem;
import com.artclassmanagement.enums.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryItemRepository extends MongoRepository<GalleryItem, String> {
    Page<GalleryItem> findByVerificationStatus(VerificationStatus status, Pageable pageable);

    Page<GalleryItem> findByIsPublicTrue(Pageable pageable);

    Page<GalleryItem> findByUploadedBy(String uploadedBy, Pageable pageable);

    Page<GalleryItem> findByIsFeaturedTrue(Pageable pageable);

    Page<GalleryItem> findByClassId(String classId, Pageable pageable);

    Page<GalleryItem> findByIsPublicTrueAndClassId(String classId, Pageable pageable);
}
