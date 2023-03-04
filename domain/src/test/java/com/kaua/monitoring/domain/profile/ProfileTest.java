package com.kaua.monitoring.domain.profile;

import com.kaua.monitoring.domain.exceptions.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

public class ProfileTest {

    @Test
    public void givenAnValidValues_whenCallsNewProfile_shouldReturnProfileCreated() {
        final var expectedUserId = "123";
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final String expectedAvatarUrl = null;

        final var aProfile = Profile.newProfile(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl);

        Assertions.assertNotNull(aProfile.getId());
        Assertions.assertEquals(expectedUserId, aProfile.getUserId());
        Assertions.assertEquals(expectedUsername, aProfile.getUsername());
        Assertions.assertEquals(expectedEmail, aProfile.getEmail());
        Assertions.assertEquals(aProfile.getType(), VersionAccountType.FREE);
        Assertions.assertNull(aProfile.getAvatarUrl());
    }

    @Test
    public void givenAnValidValues_whenCallsUpdate_shouldReturnProfileUpdated() {
        final var expectedUserId = "123";
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedAvatarUrl = "imaginaria";
        final var expectedVersionType = VersionAccountType.PREMIUM;

        final var aProfile = Profile.newProfile(
                expectedUserId,
                "ka",
                expectedEmail,
                null);

        final var actualProfile = aProfile.update(
                expectedUsername,
                expectedAvatarUrl,
                expectedVersionType
        );
        final var actualProfileValidate = actualProfile.validate();

        Assertions.assertEquals(0, actualProfileValidate.size());

        Assertions.assertNotNull(aProfile.getId());
        Assertions.assertEquals(expectedUserId, actualProfile.getUserId());
        Assertions.assertEquals(expectedUsername, actualProfile.getUsername());
        Assertions.assertEquals(expectedEmail, actualProfile.getEmail());
        Assertions.assertEquals(expectedVersionType, actualProfile.getType());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.getAvatarUrl());
    }

    @Test
    public void givenAnInvalidValues_whenCallsNewProfile_shouldReturnErrorList() {
        final String expectedUserId = null;
        final var expectedUsername = " ";
        final String expectedEmail = null;
        final var expectedAvatarUrl = "url/imaginaria";

        final var expectedErrorsMessages = List.of(
                new Error("'userId' should not be null or empty"),
                new Error("'username' should not be null or empty"),
                new Error("'email' should not be null or empty")
        );

        final var aProfile = Profile.newProfile(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl);

        final var actualExceptions = aProfile.validate();

        Assertions.assertEquals(expectedErrorsMessages, actualExceptions);
    }
}
