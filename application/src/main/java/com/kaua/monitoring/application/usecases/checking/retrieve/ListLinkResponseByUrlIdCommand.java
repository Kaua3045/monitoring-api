package com.kaua.monitoring.application.usecases.checking.retrieve;

import java.time.Instant;

public record ListLinkResponseByUrlIdCommand(
        String urlId,
        Instant startTimestamp,
        Instant endTimestamp
) {
}
