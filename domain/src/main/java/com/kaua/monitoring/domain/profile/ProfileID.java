package com.kaua.monitoring.domain.profile;

import com.kaua.monitoring.domain.Identifier;

import java.util.UUID;

public class ProfileID extends Identifier {

    private final String value;

    public ProfileID(String value) {
        this.value = value;
    }

    public static ProfileID unique() {
        return new ProfileID(UUID.randomUUID().toString().toLowerCase());
    }

    public static ProfileID from(final String anId) {
        return new ProfileID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }
}
