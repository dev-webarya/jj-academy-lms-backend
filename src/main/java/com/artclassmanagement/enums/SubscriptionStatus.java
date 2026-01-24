package com.artclassmanagement.enums;

/**
 * SubscriptionStatus - Status of a student's monthly subscription
 */
public enum SubscriptionStatus {
    ACTIVE, // Currently valid subscription
    EXPIRED, // Month has ended
    CANCELLED, // Manually cancelled by admin
    RENEWED // Auto-created for next month (historical record)
}
