package com.kaua.monitoring.application.exceptions;

import com.kaua.monitoring.domain.exceptions.Error;

public class UserIdAlreadyExistsException extends NoStackTraceException {

    public UserIdAlreadyExistsException() {
        super("UserId already exists");
    }

    public static Error with() {
        return new Error(new UserIdAlreadyExistsException().getMessage());
    }
}
