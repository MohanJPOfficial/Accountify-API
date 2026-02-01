package com.mkdevelopers.accountify.business.exception;

import com.mkdevelopers.accountify.common.exception.ResourceNotFoundException;

public class BusinessNotFoundException extends ResourceNotFoundException {

    public BusinessNotFoundException() {
    }

    public BusinessNotFoundException(String message) {
        super(message);
    }
}
