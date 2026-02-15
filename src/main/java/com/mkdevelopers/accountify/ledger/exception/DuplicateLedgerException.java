package com.mkdevelopers.accountify.ledger.exception;

import com.mkdevelopers.accountify.common.exception.DuplicateResourceException;

public class DuplicateLedgerException extends DuplicateResourceException {
    public DuplicateLedgerException(String message) {
        super(message);
    }
}
