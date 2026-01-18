package com.artclassmanagement.repository;

import com.artclassmanagement.entity.Batch;
import com.artclassmanagement.enums.BatchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends MongoRepository<Batch, String> {
    Page<Batch> findByClassId(String classId, Pageable pageable);

    Page<Batch> findByInstructorId(String instructorId, Pageable pageable);

    List<Batch> findByInstructorIdAndStatus(String instructorId, BatchStatus status);

    Page<Batch> findByStatus(BatchStatus status, Pageable pageable);

    List<Batch> findByClassIdAndStatus(String classId, BatchStatus status);

    boolean existsByBatchCode(String batchCode);
}
