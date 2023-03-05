package com.kaua.monitoring.application.usecases.link.update;

import java.time.Instant;

public record UpdateLinkCommand(
        String id,
        String title,
        String url,
        Instant executeDate,
        boolean repeat
) {
}
