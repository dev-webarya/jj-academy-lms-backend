package com.artclassmanagement.service;

import com.artclassmanagement.dto.request.SubscriptionRequestDto;
import com.artclassmanagement.dto.response.SubscriptionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service for managing student subscriptions.
 * Admin-only operations.
 */
public interface SubscriptionService {

    // CRUD operations
    SubscriptionResponseDto create(SubscriptionRequestDto request);

    SubscriptionResponseDto getById(String id);

    SubscriptionResponseDto update(String id, SubscriptionRequestDto request);

    void delete(String id);

    // Query operations
    Page<SubscriptionResponseDto> getAll(Pageable pageable);

    List<SubscriptionResponseDto> getByStudentId(String studentId);

    SubscriptionResponseDto getActiveByStudentId(String studentId);

    SubscriptionResponseDto getByStudentIdAndMonth(String studentId, Integer month, Integer year);

    Page<SubscriptionResponseDto> getActiveSubscriptions(Pageable pageable);

    List<SubscriptionResponseDto> getByMonth(Integer month, Integer year);

    // Subscription management
    SubscriptionResponseDto renewSubscription(String studentId);

    SubscriptionResponseDto cancelSubscription(String id);

    List<SubscriptionResponseDto> getOverLimitSubscriptions();

    // Monthly operations
    void expireOldSubscriptions();
}
