package com.mkdevelopers.accountify.business.exception;

import com.mkdevelopers.accountify.common.exception.DuplicateResourceException;

public class DuplicateBusinessException extends DuplicateResourceException {

    public DuplicateBusinessException(String message) {
        super(message);
    }
}
