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
        Assertions.assertNull(aProfile.getAvatarUrl());
    }
}
