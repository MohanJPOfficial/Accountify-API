package com.mkdevelopers.accountify.journal.exception;

import com.mkdevelopers.accountify.common.exception.DuplicateResourceException;

public class DuplicateJournalException extends DuplicateResourceException {

    public DuplicateJournalException(String message) {
        super(message);
    }
}
