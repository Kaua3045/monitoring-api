package com.kaua.monitoring.application.exceptions;

public class NoStackTraceException extends RuntimeException {

    public NoStackTraceException(final String message) {
        super(message, null);
    }

    public NoStackTraceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
