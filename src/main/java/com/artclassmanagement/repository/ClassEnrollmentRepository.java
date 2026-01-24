package com.artclassmanagement.repository;

import com.artclassmanagement.entity.ClassEnrollment;
import com.artclassmanagement.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ClassEnrollment - accesses ArtAcademy's class_enrollments
 * collection.
 */
@Repository
public interface ClassEnrollmentRepository extends MongoRepository<ClassEnrollment, String> {

    Page<ClassEnrollment> findByStatus(EnrollmentStatus status, Pageable pageable);

    Page<ClassEnrollment> findByClassId(String classId, Pageable pageable);

    List<ClassEnrollment> findByUserId(String userId);

    Page<ClassEnrollment> findByUserId(String userId, Pageable pageable);

    Optional<ClassEnrollment> findByUserIdAndClassId(String userId, String classId);

    boolean existsByUserIdAndClassId(String userId, String classId);

    long countByStatus(EnrollmentStatus status);
}
