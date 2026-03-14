package com.mkdevelopers.accountify.billentry.exception;

import com.mkdevelopers.accountify.common.exception.DuplicateResourceException;

public class DuplicateBillEntryException extends DuplicateResourceException {
    public DuplicateBillEntryException(String message) {
        super(message);
    }
}
