package com.kaua.monitoring.infrastructure.link.inputs;

public record CreateLinkBody(
        String title,
        String url,
        String executeDate,
        boolean repeat,
        String profileId
) {
}
