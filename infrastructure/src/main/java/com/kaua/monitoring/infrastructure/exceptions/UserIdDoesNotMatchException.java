package com.kaua.monitoring.infrastructure.exceptions;

import com.kaua.monitoring.application.exceptions.NoStackTraceException;

public class UserIdDoesNotMatchException extends NoStackTraceException {

    public UserIdDoesNotMatchException() {
        super("UserId does not match");
    }
}
