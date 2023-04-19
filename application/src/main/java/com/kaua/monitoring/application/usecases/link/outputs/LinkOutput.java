package com.kaua.monitoring.application.usecases.link.outputs;

import com.kaua.monitoring.domain.links.Link;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record LinkOutput(
        String id,
        String title,
        String url,
        String executeDateFormatted,
        String linkExecution,
        String profileId
) {

    public static LinkOutput from(final Link aLink) {
        final var aConvertToZoneId = LocalDateTime
                .ofInstant(
                        aLink.getExecuteDate(),
                        ZoneId.of("America/Sao_Paulo")
                );

        final var aFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        final var aExecuteDateFormatted = aFormatter.format(aConvertToZoneId);

        return new LinkOutput(
                aLink.getId().getValue(),
                aLink.getTitle(),
                aLink.getUrl(),
                aExecuteDateFormatted,
                aLink.getLinkExecution().getName(),
                aLink.getProfile().getId().getValue()
        );
    }
}
