package com.kaua.monitoring.domain.links;

import com.kaua.monitoring.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class LinkID extends Identifier {

    private final String value;

    public LinkID(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static LinkID unique() {
        return new LinkID(UUID.randomUUID().toString().toLowerCase());
    }

    public static LinkID from(final String aId) {
        return new LinkID(aId);
    }

    @Override
    public String getValue() {
        return value;
    }
}
