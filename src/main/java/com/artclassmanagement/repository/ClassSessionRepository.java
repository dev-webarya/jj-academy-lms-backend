package com.artclassmanagement.repository;

import com.artclassmanagement.entity.ClassSession;
import com.artclassmanagement.enums.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassSessionRepository extends MongoRepository<ClassSession, String> {
    Page<ClassSession> findByBatchId(String batchId, Pageable pageable);

    Page<ClassSession> findByInstructorId(String instructorId, Pageable pageable);

    List<ClassSession> findByBatchIdAndSessionDate(String batchId, LocalDate sessionDate);

    List<ClassSession> findByBatchIdAndSessionDateBetween(String batchId, LocalDate startDate, LocalDate endDate);

    List<ClassSession> findByInstructorIdAndSessionDate(String instructorId, LocalDate sessionDate);

    Page<ClassSession> findByStatus(SessionStatus status, Pageable pageable);

    List<ClassSession> findByBatchIdAndStatus(String batchId, SessionStatus status);
}
