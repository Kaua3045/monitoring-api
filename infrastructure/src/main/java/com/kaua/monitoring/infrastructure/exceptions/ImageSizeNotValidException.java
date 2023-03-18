package com.kaua.monitoring.infrastructure.exceptions;

import com.kaua.monitoring.application.exceptions.NoStackTraceException;

public class ImageSizeNotValidException extends NoStackTraceException {

    public ImageSizeNotValidException() {
        super("Image size is not valid");
    }
}
