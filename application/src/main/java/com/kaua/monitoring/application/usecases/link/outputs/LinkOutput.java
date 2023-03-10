package com.kaua.monitoring.application.usecases.link.outputs;

import com.kaua.monitoring.domain.links.Link;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public record LinkOutput(
        String id,
        String title,
        String url,
        String executeDateFormatted,
        String linkExecutions,
        String profileId
) {

    public static LinkOutput from(final Link aLink) {
        final var aFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.of("America/Sao_Paulo"));

        // Se tirar o plus retorna -3 horas
        final var aExecuteDateFormatted = aFormatter.format(aLink.getExecuteDate()
                .plus(3, ChronoUnit.HOURS));

        return new LinkOutput(
                aLink.getId().getValue(),
                aLink.getTitle(),
                aLink.getUrl(),
                aExecuteDateFormatted,
                aLink.getLinkExecution().name(),
                aLink.getProfile().getId().getValue()
        );
    }
}
