package com.mkdevelopers.accountify.business.exception;

public class DuplicateBusinessException extends RuntimeException{
    public DuplicateBusinessException(String message) {
        super(message);
    }
}
