package com.kaua.monitoring.domain.client;

import com.kaua.monitoring.domain.Aggregate;
import com.kaua.monitoring.domain.utils.InstantUtils;
import lombok.Getter;

import java.time.Instant;

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
}
