package com.artclassmanagement.controller;

import com.artclassmanagement.dto.request.SubscriptionRequestDto;
import com.artclassmanagement.dto.response.SubscriptionResponseDto;
import com.artclassmanagement.security.annotations.AdminOnly;
import com.artclassmanagement.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing student subscriptions.
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscriptions", description = "Student subscription management (Admin only)")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @AdminOnly
    @Operation(summary = "Create a new subscription for a student")
    public ResponseEntity<SubscriptionResponseDto> create(@Valid @RequestBody SubscriptionRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.create(request));
    }

    @GetMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Get subscription by ID")
    public ResponseEntity<SubscriptionResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(subscriptionService.getById(id));
    }

    @PutMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Update a subscription")
    public ResponseEntity<SubscriptionResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody SubscriptionRequestDto request) {
        return ResponseEntity.ok(subscriptionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Delete a subscription")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @AdminOnly
    @Operation(summary = "Get all subscriptions (paginated)")
    public ResponseEntity<Page<SubscriptionResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(subscriptionService.getAll(pageable));
    }

    @GetMapping("/active")
    @AdminOnly
    @Operation(summary = "Get all active subscriptions (paginated)")
    public ResponseEntity<Page<SubscriptionResponseDto>> getActive(Pageable pageable) {
        return ResponseEntity.ok(subscriptionService.getActiveSubscriptions(pageable));
    }

    @GetMapping("/student/{studentId}")
    @AdminOnly
    @Operation(summary = "Get all subscriptions for a student")
    public ResponseEntity<List<SubscriptionResponseDto>> getByStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(subscriptionService.getByStudentId(studentId));
    }

    @GetMapping("/student/{studentId}/active")
    @AdminOnly
    @Operation(summary = "Get active subscription for a student")
    public ResponseEntity<SubscriptionResponseDto> getActiveByStudent(@PathVariable String studentId) {
        SubscriptionResponseDto subscription = subscriptionService.getActiveByStudentId(studentId);
        return subscription != null ? ResponseEntity.ok(subscription) : ResponseEntity.notFound().build();
    }

    @GetMapping("/month/{year}/{month}")
    @AdminOnly
    @Operation(summary = "Get all subscriptions for a specific month")
    public ResponseEntity<List<SubscriptionResponseDto>> getByMonth(
            @PathVariable Integer year,
            @PathVariable Integer month) {
        return ResponseEntity.ok(subscriptionService.getByMonth(month, year));
    }

    @PostMapping("/student/{studentId}/renew")
    @AdminOnly
    @Operation(summary = "Renew subscription for next month")
    public ResponseEntity<SubscriptionResponseDto> renew(@PathVariable String studentId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.renewSubscription(studentId));
    }

    @PostMapping("/{id}/cancel")
    @AdminOnly
    @Operation(summary = "Cancel a subscription")
    public ResponseEntity<SubscriptionResponseDto> cancel(@PathVariable String id) {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(id));
    }

    @GetMapping("/over-limit")
    @AdminOnly
    @Operation(summary = "Get students who have exceeded their session limit")
    public ResponseEntity<List<SubscriptionResponseDto>> getOverLimit() {
        return ResponseEntity.ok(subscriptionService.getOverLimitSubscriptions());
    }
}
