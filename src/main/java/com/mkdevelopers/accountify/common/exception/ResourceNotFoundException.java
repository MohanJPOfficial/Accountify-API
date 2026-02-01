package com.mkdevelopers.accountify.common.exception;

/**
 * Base exception for all "resource not found" errors.
 * All module-specific NotFoundExceptions should extend this class.
 */
public abstract class ResourceNotFoundException extends RuntimeException {

    protected ResourceNotFoundException() {
    }

    protected ResourceNotFoundException(String message) {
        super(message);
    }
}
