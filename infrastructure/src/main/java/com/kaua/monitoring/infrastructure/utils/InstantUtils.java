package com.kaua.monitoring.infrastructure.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class InstantUtils {

    private InstantUtils() {}

    public static Instant parseToLocalTimeWithZonedInstant(final String date) {
        return LocalDateTime.parse(date)
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toInstant();
    }
}
