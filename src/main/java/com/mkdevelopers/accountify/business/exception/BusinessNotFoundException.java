package com.mkdevelopers.accountify.business.exception;

public class BusinessNotFoundException extends RuntimeException {

    public BusinessNotFoundException() {

    }

    public BusinessNotFoundException(String message) {
        super(message);
    }
}
