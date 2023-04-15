package com.kaua.monitoring.domain.profile;

import com.kaua.monitoring.domain.exceptions.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ProfileTest {

    @Test
    public void givenAnValidValues_whenCallsNewProfile_shouldReturnProfileCreated() {
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedPassword = "12345678";
        final String expectedAvatarUrl = null;

        final var aProfile = Profile.newProfile(
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl);

        Assertions.assertNotNull(aProfile.getId());
        Assertions.assertEquals(expectedUsername, aProfile.getUsername());
        Assertions.assertEquals(expectedEmail, aProfile.getEmail());
        Assertions.assertEquals(expectedPassword, aProfile.getPassword());
        Assertions.assertEquals(aProfile.getType(), VersionAccountType.FREE);
        Assertions.assertNull(aProfile.getAvatarUrl());
    }

    @Test
    public void givenAnValidValues_whenCallsUpdate_shouldReturnProfileUpdated() {
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedPassword = "12345678";
        final var expectedAvatarUrl = "imaginaria";
        final var expectedVersionType = VersionAccountType.PREMIUM;

        final var aProfile = Profile.newProfile(
                "ka",
                expectedEmail,
                "012358679",
                null);

        final var actualProfile = aProfile.update(
                expectedUsername,
                expectedPassword,
                expectedAvatarUrl,
                expectedVersionType
        );
        final var actualProfileValidate = actualProfile.validate();

        Assertions.assertEquals(0, actualProfileValidate.size());

        Assertions.assertNotNull(aProfile.getId());
        Assertions.assertEquals(expectedUsername, actualProfile.getUsername());
        Assertions.assertEquals(expectedEmail, actualProfile.getEmail());
        Assertions.assertEquals(expectedPassword, actualProfile.getPassword());
        Assertions.assertEquals(expectedVersionType, actualProfile.getType());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.getAvatarUrl());
    }

    @Test
    public void givenAnInvalidValues_whenCallsNewProfile_shouldReturnErrorList() {
        final var expectedUsername = " ";
        final String expectedEmail = null;
        final String expectedPassword = null;
        final var expectedAvatarUrl = "url/imaginaria";

        final var expectedErrorsMessages = List.of(
                new Error("'username' should not be null or empty"),
                new Error("'email' should not be null or empty"),
                new Error("'password' should not be null or empty")
        );

        final var aProfile = Profile.newProfile(
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl);

        final var actualExceptions = aProfile.validate();

        Assertions.assertEquals(expectedErrorsMessages, actualExceptions);
    }

    @Test
    public void givenAnInvalidValuesAndPasswordNotContainsMinimumValue_whenCallsNewProfile_shouldReturnErrorList() {
        final var expectedUsername = " ";
        final String expectedEmail = null;
        final var expectedPassword = "12345";
        final var expectedAvatarUrl = "url/imaginaria";

        final var expectedErrorsMessages = List.of(
                new Error("'username' should not be null or empty"),
                new Error("'email' should not be null or empty"),
                new Error("'password' must be at least 8 characters")
        );

        final var aProfile = Profile.newProfile(
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl);

        final var actualExceptions = aProfile.validate();

        Assertions.assertEquals(expectedErrorsMessages, actualExceptions);
    }
}
