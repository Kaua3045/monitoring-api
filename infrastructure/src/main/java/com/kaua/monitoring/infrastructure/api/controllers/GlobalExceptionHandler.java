package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.infrastructure.exceptions.ImageSizeNotValidException;
import com.kaua.monitoring.infrastructure.exceptions.ImageTypeNotValidException;
import com.kaua.monitoring.infrastructure.exceptions.UserIdDoesNotMatchException;
import com.kaua.monitoring.infrastructure.utils.ApiError;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(final BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiError(ex.getMessage(), null));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(final ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiError(ex.getMessage(), null));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(ex.getMessage(), ex.getErrors()));
    }

    @ExceptionHandler(UserIdDoesNotMatchException.class)
    public ResponseEntity<?> handleUserIdDoesNotMatchException(final UserIdDoesNotMatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(ex.getMessage(), Collections.emptyList()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(final NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(ex.getMessage(), Collections.emptyList()));
    }

    @ExceptionHandler(ImageTypeNotValidException.class)
    public ResponseEntity<?> handleImageTypeNotValidException(final ImageTypeNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(ex.getMessage(), Collections.emptyList()));
    }

    @ExceptionHandler(ImageSizeNotValidException.class)
    public ResponseEntity<?> handleImageSizeNotValidException(final ImageSizeNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(ex.getMessage(), Collections.emptyList()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(final Exception ex) {
        log.error("Internal server error -> {}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("Erro inesperado", Collections.emptyList()));
    }
}
