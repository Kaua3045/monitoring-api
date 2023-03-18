package com.kaua.monitoring.application.usecases.checking.outputs;

import com.kaua.monitoring.domain.checking.LinkResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record LinkResponseOutput(
        String id,
        String responseMessage,
        int statusCode,
        String verifiedDate,
        String urlId
) {

    public static LinkResponseOutput from(final LinkResponse aLink) {
        final var aConvertToZoneId = LocalDateTime
                .ofInstant(
                        aLink.getVerifiedDate(),
                        ZoneId.of("America/Sao_Paulo")
                );

        final var aFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        final var aVerifiedDate = aFormatter.format(aConvertToZoneId);

        return new LinkResponseOutput(
                aLink.getId().getValue(),
                aLink.getResponseMessage(),
                aLink.getStatusCode(),
                aVerifiedDate,
                aLink.getLink().getId().getValue()
        );
    }
}
