package com.kaua.monitoring.application.usecases.checking.outputs;

import com.kaua.monitoring.domain.checking.LinkResponse;

public record LinkResponseOutput(
        String id,
        String responseMessage,
        int statusCode,
        String urlId
) {

    public static LinkResponseOutput from(final LinkResponse aLink) {
        return new LinkResponseOutput(
                aLink.getId().getValue(),
                aLink.getResponseMessage(),
                aLink.getStatusCode(),
                aLink.getLink().getId().getValue()
        );
    }
}
