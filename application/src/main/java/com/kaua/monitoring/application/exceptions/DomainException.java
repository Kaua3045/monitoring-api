package com.kaua.monitoring.application.exceptions;

import com.kaua.monitoring.domain.exceptions.Error;
import lombok.Getter;

import java.util.List;

@Getter
public class DomainException extends NoStackTraceException {

    protected final List<Error> errors;

    protected DomainException(final List<Error> errors) {
        super("Domain Exception");
        this.errors = errors;
    }

    public static DomainException with(final Error anError) {
        return new DomainException(List.of(anError));
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException(anErrors);
    }
}
