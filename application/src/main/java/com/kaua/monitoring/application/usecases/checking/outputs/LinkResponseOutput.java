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
        String requestTime,
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

        final var aRequestTimeVerify = aLink.getRequestTime() == null ? Integer.valueOf(0) : aLink.getRequestTime();

        final var aRequestTime = aRequestTimeVerify.toString().length() <= 3
                ? "0." + aRequestTimeVerify + "ms"
                : new StringBuilder(aRequestTimeVerify.toString()).insert(1, ".") + "s";

        return new LinkResponseOutput(
                aLink.getId().getValue(),
                aLink.getResponseMessage(),
                aLink.getStatusCode(),
                aVerifiedDate,
                aRequestTime,
                aLink.getLink().getId().getValue()
        );
    }
}
