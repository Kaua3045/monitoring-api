package com.kaua.monitoring.application.exceptions;

public class EmailAlreadyExistsException extends NoStackTraceException {

    public EmailAlreadyExistsException() {
        super("Email already exists");
    }
}
