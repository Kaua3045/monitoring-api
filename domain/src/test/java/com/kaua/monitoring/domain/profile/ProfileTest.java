package com.kaua.monitoring.domain.profile;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProfileTest {

    @Test
    public void givenAnValidValues_whenCallsNewProfile_shouldReturnProfileCreated() {
        final var expectedUserId = "123";
        final String expectedAvatarUrl = null;

        final var aProfile = Profile.newProfile(expectedUserId, expectedAvatarUrl);

        Assertions.assertNotNull(aProfile.getId());
        Assertions.assertEquals(expectedUserId, aProfile.getUserId());
        Assertions.assertEquals(aProfile.getType(), VersionAccountType.FREE);
        Assertions.assertNull(aProfile.getAvatarUrl());
    }

    @Test
    public void givenAnInvalidValues_whenCallsNewProfile_shouldReturnErrorList() {
        final String expectedUserId = null;
        final var expectedAvatarUrl = "url/imaginaria";

        final var expectedErrorMessage = "'userId' should not be null or empty";

        final var aProfile = Profile.newProfile(expectedUserId, expectedAvatarUrl);

        final var actualExceptions = aProfile.validate();

        Assertions.assertEquals(expectedErrorMessage, actualExceptions.get(0).message());
    }
}
