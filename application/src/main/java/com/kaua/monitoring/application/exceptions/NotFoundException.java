package com.kaua.monitoring.application.exceptions;

public class NotFoundException extends NoStackTraceException {

    public NotFoundException(final Class<?> clazz, final String userId) {
        super("%s with ID %s was not found".formatted(clazz.getSimpleName(), userId));
    }
}
