package com.artclassmanagement.enums;

public enum EnrollmentStatus {
    PENDING, // Student enrolled, awaiting approval
    APPROVED, // Enrollment approved
    REJECTED, // Admin rejected enrollment (e.g., already enrolled in class)
    ASSIGNED, // Assigned to a batch
    IN_PROGRESS, // Currently attending
    COMPLETED, // Course completed
    CANCELLED, // Enrollment cancelled
    DROPPED // Student dropped out
}
