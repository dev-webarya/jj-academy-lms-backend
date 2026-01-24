package com.artclassmanagement.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Security annotation for endpoints accessible by ADMIN or INSTRUCTOR roles.
 * Use this for instructor-level operations like session management, attendance
 * marking.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
public @interface InstructorOnly {
}
