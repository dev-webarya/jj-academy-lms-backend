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

    // Find sessions by date
    List<ClassSession> findBySessionDate(LocalDate sessionDate);

    // Find sessions by date range
    List<ClassSession> findBySessionDateBetween(LocalDate startDate, LocalDate endDate);

    // Find upcoming sessions (date >= today)
    Page<ClassSession> findBySessionDateGreaterThanEqualAndStatus(
            LocalDate date, SessionStatus status, Pageable pageable);

    // Find sessions by status
    Page<ClassSession> findByStatus(SessionStatus status, Pageable pageable);

    // Find sessions ordered by date
    Page<ClassSession> findAllByOrderBySessionDateDesc(Pageable pageable);

    // Count sessions for a date range
    long countBySessionDateBetween(LocalDate startDate, LocalDate endDate);
}
