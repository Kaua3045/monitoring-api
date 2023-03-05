package com.kaua.monitoring.application.exceptions;

public class NoStackTraceException extends RuntimeException {

    public NoStackTraceException(String message) {
        super(message);
    }

    public NoStackTraceException(String message, Throwable cause) {
        super(message, cause);
    }
}
