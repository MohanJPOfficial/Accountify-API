package com.mkdevelopers.accountify.entry.exception;

import com.mkdevelopers.accountify.common.exception.ResourceNotFoundException;

public class EntryNotFoundException extends ResourceNotFoundException {
    public EntryNotFoundException(String message) {
        super(message);
    }
}
