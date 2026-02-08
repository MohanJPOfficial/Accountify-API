package com.mkdevelopers.accountify.entry.exception;

import com.mkdevelopers.accountify.common.exception.DuplicateResourceException;

public class DuplicateEntryException extends DuplicateResourceException {
    public DuplicateEntryException(String message) {
        super(message);
    }
}
