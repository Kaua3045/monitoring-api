package com.kaua.monitoring.application.usecases.link.outputs;

import com.kaua.monitoring.domain.links.Link;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

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
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.of("pt", "BR"))
                .withZone(ZoneId.systemDefault());

        final var aExecuteDateFormatted = aFormatter.format(aLink.getExecuteDate());

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
