package com.mkdevelopers.accountify.common.exception;

/**
 * Base exception for all "duplicate resource" errors.
 * All module-specific DuplicateExceptions should extend this class.
 */
public abstract class DuplicateResourceException extends RuntimeException {

    protected DuplicateResourceException(String message) {
        super(message);
    }
}
