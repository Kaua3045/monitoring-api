package com.kaua.monitoring.infrastructure.link.inputs;

public record UpdateLinkBody(
        String id,
        String title,
        String url,
        String executeDate,
        String linkExecution
) {
}
