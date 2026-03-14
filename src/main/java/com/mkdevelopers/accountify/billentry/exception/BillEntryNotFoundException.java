package com.mkdevelopers.accountify.billentry.exception;

import com.mkdevelopers.accountify.common.exception.ResourceNotFoundException;

public class BillEntryNotFoundException extends ResourceNotFoundException {
    public BillEntryNotFoundException(String message) {
        super(message);
    }
}
