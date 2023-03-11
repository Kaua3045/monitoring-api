package com.kaua.monitoring.infrastructure.link.inputs;

public record UpdateLinkBody(
        String title,
        String url,
        String executeDate,
        String linkExecution
) {
}
