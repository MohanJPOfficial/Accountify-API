package com.mkdevelopers.accountify.bill.exception;

import com.mkdevelopers.accountify.common.exception.DuplicateResourceException;

public class DuplicateBillException extends DuplicateResourceException {
    public DuplicateBillException(String message) {
        super(message);
    }
}
