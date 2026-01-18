package com.artclassmanagement.repository;

import com.artclassmanagement.entity.Attendance;
import com.artclassmanagement.enums.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findBySessionId(String sessionId);

    Page<Attendance> findByStudentId(String studentId, Pageable pageable);

    List<Attendance> findByStudentIdAndBatchId(String studentId, String batchId);

    Optional<Attendance> findBySessionIdAndStudentId(String sessionId, String studentId);

    long countBySessionIdAndStatus(String sessionId, AttendanceStatus status);

    long countByStudentIdAndBatchIdAndStatus(String studentId, String batchId, AttendanceStatus status);

    long countByStudentIdAndBatchId(String studentId, String batchId);

    boolean existsBySessionIdAndStudentId(String sessionId, String studentId);
}
