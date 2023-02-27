package com.kaua.monitoring.domain.client;

import com.kaua.monitoring.domain.exceptions.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    public void givenAnInvalidValues_whenCallsNewClient_shouldReturnExceptionList() {
        final String expectedName = null;
        final var expectedEmail = " ";
        final var expectedPassword = "1234567";

        final var expectedExceptionList = List.of(
                new Error("'name' should not be null or empty"),
                new Error("'email' should not be null or empty"),
                new Error("'password' must contain 8 characters at least")
        );
        final var expectedExceptionCount = 3;

        final var aClient = Client.newClient(
                expectedName,
                expectedEmail,
                expectedPassword
        );

        final var actualExceptions = aClient.validate();

        Assertions.assertEquals(expectedExceptionList, actualExceptions);
        Assertions.assertEquals(expectedExceptionCount, actualExceptions.size());
    }
}
