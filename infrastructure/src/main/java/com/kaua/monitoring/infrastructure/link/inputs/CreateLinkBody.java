package com.kaua.monitoring.infrastructure.link.inputs;

public record CreateLinkBody(
        String title,
        String url,
        long executeDate,
        String repeat,
        String profileId
) {
}
