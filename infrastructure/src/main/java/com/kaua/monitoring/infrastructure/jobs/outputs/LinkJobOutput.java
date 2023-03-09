package com.kaua.monitoring.infrastructure.jobs.outputs;

import lombok.Data;

@Data
public class LinkJobOutput {

    private String id;
    private String urlId;
    private String responseMessage;
    private int statusCode;

    public LinkJobOutput() {}

    public LinkJobOutput(
            final String id,
            final String urlId,
            final String responseMessage,
            final int statusCode
    ) {
        this.id = id;
        this.urlId = urlId;
        this.responseMessage = responseMessage;
        this.statusCode = statusCode;
    }
}
