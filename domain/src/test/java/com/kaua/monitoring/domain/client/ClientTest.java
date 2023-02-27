package com.kaua.monitoring.domain.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientTest {

    @Test
    public void givenAnValidValues_whenCallsNewClient_shouldReturnClientCreated() {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "12345678";
        final var expectedType = ClientType.FREE;

        final var aClient = Client.newClient(
                expectedName,
                expectedEmail,
                expectedPassword
        );

        Assertions.assertNotNull(aClient.getId());
        Assertions.assertEquals(expectedName, aClient.getName());
        Assertions.assertEquals(expectedEmail, aClient.getEmail());
        Assertions.assertEquals(expectedPassword, aClient.getPassword());
        Assertions.assertNull(aClient.getAvatarUrl());
        Assertions.assertFalse(aClient.isEmailVerified());
        Assertions.assertEquals(expectedType, aClient.getType());
    }
}
