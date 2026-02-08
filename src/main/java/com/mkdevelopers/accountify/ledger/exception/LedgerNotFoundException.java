package com.mkdevelopers.accountify.ledger.exception;

import com.mkdevelopers.accountify.common.exception.ResourceNotFoundException;

public class LedgerNotFoundException extends ResourceNotFoundException {
    public LedgerNotFoundException(String message) {
        super(message);
    }
}
