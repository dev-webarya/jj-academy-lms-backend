package com.artclassmanagement.repository;

import com.artclassmanagement.entity.Attendance;
import com.artclassmanagement.enums.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    // Find all attendance for a session
    List<Attendance> findBySessionId(String sessionId);

    // Find attendance by student with pagination
    Page<Attendance> findByStudentId(String studentId, Pageable pageable);

    // Find specific attendance record
    Optional<Attendance> findBySessionIdAndStudentId(String sessionId, String studentId);

    // Check if attendance exists
    boolean existsBySessionIdAndStudentId(String sessionId, String studentId);

    // Count by session and status
    long countBySessionIdAndStatus(String sessionId, AttendanceStatus status);

    // Monthly attendance for a student
    List<Attendance> findByStudentIdAndSessionMonthAndSessionYear(
            String studentId, Integer month, Integer year);

    // Count sessions attended by student in a month (only PRESENT status counts)
    long countByStudentIdAndSessionMonthAndSessionYearAndStatus(
            String studentId, Integer month, Integer year, AttendanceStatus status);

    // Find over-limit students for a month
    @Query("{ 'sessionMonth': ?0, 'sessionYear': ?1, 'isOverLimit': true }")
    List<Attendance> findOverLimitByMonthAndYear(Integer month, Integer year);

    // Find by subscription
    List<Attendance> findBySubscriptionId(String subscriptionId);
}
