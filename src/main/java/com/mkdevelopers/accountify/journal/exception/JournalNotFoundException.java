package com.mkdevelopers.accountify.journal.exception;

import com.mkdevelopers.accountify.common.exception.ResourceNotFoundException;

public class JournalNotFoundException extends ResourceNotFoundException {

    public JournalNotFoundException() {
    }

    public JournalNotFoundException(String message) {
        super(message);
    }
}
