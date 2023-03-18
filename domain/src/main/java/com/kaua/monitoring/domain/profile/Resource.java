package com.kaua.monitoring.domain.profile;

import lombok.Data;

import java.io.InputStream;
import java.util.Objects;

@Data
public class Resource {

    private final InputStream inputStream;
    private final String contentType;
    private final String name;

    public Resource(
            final InputStream inputStream,
            final String contentType,
            final String name
    ) {
        this.inputStream = Objects.requireNonNull(inputStream);
        this.contentType = Objects.requireNonNull(contentType);
        this.name = Objects.requireNonNull(name);
    }

    public static Resource with(
            final InputStream inputStream,
            final String contentType,
            final String name
    ) {
        return new Resource(inputStream, contentType, name);
    }
}
