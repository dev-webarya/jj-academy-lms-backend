package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.request.SubscriptionRequestDto;
import com.artclassmanagement.dto.response.SubscriptionResponseDto;
import com.artclassmanagement.entity.StudentSubscription;
import com.artclassmanagement.entity.User;
import com.artclassmanagement.enums.SubscriptionStatus;
import com.artclassmanagement.exception.BadRequestException;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.mapper.SubscriptionMapper;
import com.artclassmanagement.repository.StudentSubscriptionRepository;
import com.artclassmanagement.repository.UserRepository;
import com.artclassmanagement.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for student subscription management.
 * Handles monthly subscriptions with session limits.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final StudentSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public SubscriptionResponseDto create(SubscriptionRequestDto request) {
        log.info("Creating subscription for student: {} for {}/{}",
                request.getStudentId(), request.getSubscriptionMonth(), request.getSubscriptionYear());

        // Validate student exists
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getStudentId()));

        // Check if subscription already exists for this month
        if (subscriptionRepository.findByStudentIdAndSubscriptionMonthAndSubscriptionYear(
                request.getStudentId(), request.getSubscriptionMonth(), request.getSubscriptionYear()).isPresent()) {
            throw new BadRequestException("Subscription already exists for this month");
        }

        // Calculate start and end dates
        YearMonth yearMonth = YearMonth.of(request.getSubscriptionYear(), request.getSubscriptionMonth());
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        StudentSubscription subscription = StudentSubscription.builder()
                .studentId(request.getStudentId())
                .studentName(student.getFullName())
                .studentEmail(student.getEmail())
                .studentPhone(student.getPhoneNumber())
                .subscriptionMonth(request.getSubscriptionMonth())
                .subscriptionYear(request.getSubscriptionYear())
                .startDate(startDate)
                .endDate(endDate)
                .allowedSessions(request.getAllowedSessions() != null ? request.getAllowedSessions() : 8)
                .attendedSessions(0)
                .status(SubscriptionStatus.ACTIVE)
                .paymentId(request.getPaymentId())
                .amountPaid(request.getAmountPaid())
                .notes(request.getNotes())
                .build();

        StudentSubscription saved = subscriptionRepository.save(subscription);
        log.info("Created subscription with ID: {}", saved.getId());

        return subscriptionMapper.toDto(saved);
    }

    @Override
    public SubscriptionResponseDto getById(String id) {
        return subscriptionMapper.toDto(findById(id));
    }

    @Override
    public SubscriptionResponseDto update(String id, SubscriptionRequestDto request) {
        log.info("Updating subscription: {}", id);
        StudentSubscription subscription = findById(id);

        if (request.getAllowedSessions() != null) {
            subscription.setAllowedSessions(request.getAllowedSessions());
        }
        if (request.getPaymentId() != null) {
            subscription.setPaymentId(request.getPaymentId());
        }
        if (request.getAmountPaid() != null) {
            subscription.setAmountPaid(request.getAmountPaid());
        }
        if (request.getNotes() != null) {
            subscription.setNotes(request.getNotes());
        }

        return subscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }

    @Override
    public void delete(String id) {
        log.warn("Deleting subscription: {}", id);
        StudentSubscription subscription = findById(id);
        subscriptionRepository.delete(subscription);
    }

    @Override
    public Page<SubscriptionResponseDto> getAll(Pageable pageable) {
        return subscriptionRepository.findAll(pageable).map(subscriptionMapper::toDto);
    }

    @Override
    public List<SubscriptionResponseDto> getByStudentId(String studentId) {
        return subscriptionRepository.findByStudentIdOrderBySubscriptionYearDescSubscriptionMonthDesc(studentId)
                .stream().map(subscriptionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public SubscriptionResponseDto getActiveByStudentId(String studentId) {
        return subscriptionRepository.findByStudentIdAndStatus(studentId, SubscriptionStatus.ACTIVE)
                .map(subscriptionMapper::toDto).orElse(null);
    }

    @Override
    public SubscriptionResponseDto getByStudentIdAndMonth(String studentId, Integer month, Integer year) {
        return subscriptionRepository.findByStudentIdAndSubscriptionMonthAndSubscriptionYear(studentId, month, year)
                .map(subscriptionMapper::toDto).orElse(null);
    }

    @Override
    public Page<SubscriptionResponseDto> getActiveSubscriptions(Pageable pageable) {
        return subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE, pageable)
                .map(subscriptionMapper::toDto);
    }

    @Override
    public List<SubscriptionResponseDto> getByMonth(Integer month, Integer year) {
        return subscriptionRepository.findBySubscriptionMonthAndSubscriptionYear(month, year)
                .stream().map(subscriptionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public SubscriptionResponseDto renewSubscription(String studentId) {
        log.info("Renewing subscription for student: {}", studentId);

        // Get current date for next month calculation
        LocalDate now = LocalDate.now();
        int nextMonth = now.getMonthValue() == 12 ? 1 : now.getMonthValue() + 1;
        int nextYear = now.getMonthValue() == 12 ? now.getYear() + 1 : now.getYear();

        // Check if subscription already exists for next month
        if (subscriptionRepository.findByStudentIdAndSubscriptionMonthAndSubscriptionYear(
                studentId, nextMonth, nextYear).isPresent()) {
            throw new BadRequestException("Subscription already exists for next month");
        }

        SubscriptionRequestDto request = SubscriptionRequestDto.builder()
                .studentId(studentId)
                .subscriptionMonth(nextMonth)
                .subscriptionYear(nextYear)
                .allowedSessions(8)
                .build();

        return create(request);
    }

    @Override
    public SubscriptionResponseDto cancelSubscription(String id) {
        log.info("Cancelling subscription: {}", id);
        StudentSubscription subscription = findById(id);
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        return subscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }

    @Override
    public List<SubscriptionResponseDto> getOverLimitSubscriptions() {
        // Get all active subscriptions where attendedSessions > allowedSessions
        return subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE).stream()
                .filter(s -> s.getAttendedSessions() > s.getAllowedSessions())
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "0 0 0 1 * *") // Run at midnight on first day of each month
    public void expireOldSubscriptions() {
        log.info("Running scheduled task to expire old subscriptions");
        LocalDate now = LocalDate.now();
        int previousMonth = now.getMonthValue() == 1 ? 12 : now.getMonthValue() - 1;
        int previousYear = now.getMonthValue() == 1 ? now.getYear() - 1 : now.getYear();

        List<StudentSubscription> oldSubscriptions = subscriptionRepository
                .findBySubscriptionMonthAndSubscriptionYear(previousMonth, previousYear);

        for (StudentSubscription subscription : oldSubscriptions) {
            if (subscription.getStatus() == SubscriptionStatus.ACTIVE) {
                subscription.setStatus(SubscriptionStatus.EXPIRED);
                subscriptionRepository.save(subscription);
                log.info("Expired subscription: {}", subscription.getId());
            }
        }
    }

    private StudentSubscription findById(String id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StudentSubscription", "id", id));
    }
}
