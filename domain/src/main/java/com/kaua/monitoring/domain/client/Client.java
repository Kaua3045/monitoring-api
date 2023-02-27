package com.kaua.monitoring.domain.client;

import com.kaua.monitoring.domain.Aggregate;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.utils.InstantUtils;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Client extends Aggregate<ClientID> {

    private final String name;
    private final String email;
    private final String password;
    private final String avatarUrl;
    private final boolean emailVerified;
    private final ClientType type;
    private final Instant createdAt;
    private final Instant updatedAt;

    private final int PASSWORD_MIN_LENGTH = 8;

    public Client(
            final ClientID clientID,
            final String name,
            final String email,
            final String password,
            final String avatarUrl,
            final boolean emailVerified,
            final ClientType type,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        super(clientID);
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.emailVerified = emailVerified;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Client newClient(
            final String aName,
            final String aEmail,
            final String aPassword
    ) {
        final var aId = ClientID.unique();
        final var now = InstantUtils.now();

        return new Client(
                aId,
                aName,
                aEmail,
                aPassword,
                null,
                false,
                ClientType.FREE,
                now,
                now
        );
    }

    @Override
    public List<Error> validate() {
        final List<Error> errors = new ArrayList<>();

        if (name == null || name.isBlank()) {
            errors.add(new Error("'name' should not be null or empty"));
        }

        if (email == null || email.isBlank()) {
            errors.add(new Error("'email' should not be null or empty"));
        }

        if (password == null || password.isBlank()) {
            errors.add(new Error("'password' should not be null or empty"));
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            errors.add(new Error("'password' must contain 8 characters at least"));
        }
        return errors;
    }
}
