package com.artclassmanagement.exception;

/**
 * Exception for bad/invalid request data.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
