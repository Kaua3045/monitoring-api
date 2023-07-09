package com.kaua.monitoring.infrastructure.exceptions;

import com.kaua.monitoring.application.exceptions.NoStackTraceException;

public class ImageTypeNotValidException extends NoStackTraceException {

    public ImageTypeNotValidException() {
        super("Image type is not valid");
    }
}
