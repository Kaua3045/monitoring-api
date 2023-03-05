package com.kaua.monitoring.infrastructure.utils;

import com.kaua.monitoring.domain.exceptions.Error;

import java.util.List;

public record ApiError(String message, List<Error> errors) {
}
