package com.kaua.monitoring.application.usecases.link.create;

import java.time.Instant;

public record CreateLinkCommand(
        String title,
        String url,
        Instant executeDate,
        boolean repeat,
        String profileId
) {
}
