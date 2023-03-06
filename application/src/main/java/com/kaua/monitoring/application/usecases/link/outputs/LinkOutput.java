package com.kaua.monitoring.application.usecases.link.outputs;

import com.kaua.monitoring.domain.links.Link;

import java.time.format.DateTimeFormatter;

public record LinkOutput(
        String id,
        String title,
        String url,
        String executeDateFormatted,
        boolean repeat,
        String profileId
) {

    public static LinkOutput from(final Link aLink) {
        final var aFormatter = DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss");

        final var aExecuteDateFormatted = aLink.getExecuteDate().format(aFormatter);

        return new LinkOutput(
                aLink.getId().getValue(),
                aLink.getTitle(),
                aLink.getUrl(),
                aExecuteDateFormatted,
                aLink.isRepeat(),
                aLink.getProfile().getId().getValue()
        );
    }
}
