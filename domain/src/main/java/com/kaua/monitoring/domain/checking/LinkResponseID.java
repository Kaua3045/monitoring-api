package com.kaua.monitoring.domain.checking;

import com.kaua.monitoring.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class LinkResponseID extends Identifier {

    private final String value;

    public LinkResponseID(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static LinkResponseID unique() {
        return new LinkResponseID(UUID.randomUUID().toString().toLowerCase());
    }

    public static LinkResponseID from(final String aId) {
        return new LinkResponseID(aId);
    }

    @Override
    public String getValue() {
        return value;
    }
}
