package com.kaua.monitoring.application.exceptions;

import com.kaua.monitoring.domain.exceptions.Error;

public class EmailAlreadyExistsException extends NoStackTraceException {

    public EmailAlreadyExistsException() {
        super("Email already exists");
    }

    public static Error with() {
        return new Error(new EmailAlreadyExistsException().getMessage());
    }
}
