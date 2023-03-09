package com.kaua.monitoring.infrastructure.jobs.reader;

import lombok.Data;

import java.time.Instant;

@Data
public class LinkJobReader {

    private String id;
    private String title;
    private String url;
    private Instant executeDate;
    private boolean repeat;
    private String profileId;

    public LinkJobReader() {}

    public LinkJobReader(
            final String id,
            final String title,
            final String url,
            final Instant executeDate,
            final boolean repeat,
            final String profileId
    ) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.executeDate = executeDate;
        this.repeat = repeat;
        this.profileId = profileId;
    }
}
