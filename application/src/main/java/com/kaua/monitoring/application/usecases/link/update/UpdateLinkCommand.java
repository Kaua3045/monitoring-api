package com.kaua.monitoring.application.usecases.link.update;

import java.time.LocalDateTime;

public record UpdateLinkCommand(
        String id,
        String title,
        String url,
        LocalDateTime executeDate,
        boolean repeat
) {
}
