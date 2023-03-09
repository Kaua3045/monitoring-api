package com.kaua.monitoring.application.usecases.link.create;

import java.time.LocalDateTime;

public record CreateLinkCommand(
        String title,
        String url,
        LocalDateTime executeDate,
        boolean repeat,
        String profileId
) {
}
