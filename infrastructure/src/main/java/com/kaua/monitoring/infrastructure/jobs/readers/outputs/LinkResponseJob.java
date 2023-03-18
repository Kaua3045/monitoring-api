package com.kaua.monitoring.infrastructure.jobs.readers.outputs;

import lombok.Data;

import java.io.Serializable;

@Data
public class LinkResponseJob implements Serializable {

    private String id;
    private String url;

    public LinkResponseJob() {}

    public LinkResponseJob(final String id, final String url) {
        this.id = id;
        this.url = url;
    }
}
