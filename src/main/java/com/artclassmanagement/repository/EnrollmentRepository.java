package com.artclassmanagement.repository;

import com.artclassmanagement.entity.Enrollment;
import com.artclassmanagement.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
    Page<Enrollment> findByUserId(String userId, Pageable pageable);

    List<Enrollment> findByUserIdAndStatus(String userId, EnrollmentStatus status);

    Page<Enrollment> findByClassId(String classId, Pageable pageable);

    Page<Enrollment> findByBatchId(String batchId, Pageable pageable);

    List<Enrollment> findByBatchIdAndStatus(String batchId, EnrollmentStatus status);

    Page<Enrollment> findByStatus(EnrollmentStatus status, Pageable pageable);

    Optional<Enrollment> findByUserIdAndClassId(String userId, String classId);

    boolean existsByUserIdAndClassId(String userId, String classId);

    long countByBatchId(String batchId);

    long countByBatchIdAndStatus(String batchId, EnrollmentStatus status);
}
