package com.kaua.monitoring.application.exceptions;

import com.kaua.monitoring.domain.exceptions.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {

    private final List<Error> errors;

    public DomainException(final List<Error> aErrors) {
        super("Domain Exception");
        this.errors = aErrors;
    }

    public static DomainException with(final Error error) {
        return new DomainException(List.of(error));
    }

    public static DomainException with(final List<Error> aErrors) {
        return new DomainException(aErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
