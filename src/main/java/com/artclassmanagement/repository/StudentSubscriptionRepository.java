package com.artclassmanagement.repository;

import com.artclassmanagement.entity.StudentSubscription;
import com.artclassmanagement.enums.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentSubscriptionRepository extends MongoRepository<StudentSubscription, String> {

    // Find active subscription for a student
    Optional<StudentSubscription> findByStudentIdAndStatus(String studentId, SubscriptionStatus status);

    // Find subscription for a specific month/year
    Optional<StudentSubscription> findByStudentIdAndSubscriptionMonthAndSubscriptionYear(
            String studentId, Integer month, Integer year);

    // Get all active subscriptions
    List<StudentSubscription> findByStatus(SubscriptionStatus status);

    // Get all subscriptions for a student
    List<StudentSubscription> findByStudentIdOrderBySubscriptionYearDescSubscriptionMonthDesc(String studentId);

    // Get paginated subscriptions by status
    Page<StudentSubscription> findByStatus(SubscriptionStatus status, Pageable pageable);

    // Get all subscriptions paginated
    Page<StudentSubscription> findAll(Pageable pageable);

    // Find subscriptions for a specific month/year
    List<StudentSubscription> findBySubscriptionMonthAndSubscriptionYear(Integer month, Integer year);

    // Find over-limit subscriptions (attended > allowed)
    @Query("{ 'attendedSessions': { $gt: 'allowedSessions' } }")
    List<StudentSubscription> findOverLimitSubscriptions();

    // Check if student has active subscription
    boolean existsByStudentIdAndStatus(String studentId, SubscriptionStatus status);
}
