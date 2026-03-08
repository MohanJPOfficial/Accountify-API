package com.mkdevelopers.accountify.bill.exception;

import com.mkdevelopers.accountify.common.exception.ResourceNotFoundException;

public class BillNotFoundException extends ResourceNotFoundException {
    public BillNotFoundException(String message) {
        super(message);
    }
}
