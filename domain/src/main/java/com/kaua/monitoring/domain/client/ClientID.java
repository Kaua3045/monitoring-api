package com.kaua.monitoring.domain.client;

import com.kaua.monitoring.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class ClientID extends Identifier {

    private final String value;

    public ClientID(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static ClientID unique() {
        return new ClientID(UUID.randomUUID().toString().toLowerCase());
    }

    public static ClientID from(final String id) {
        return new ClientID(id);
    }

    public static ClientID from(final UUID id) {
        return new ClientID(id.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return null;
    }
}
