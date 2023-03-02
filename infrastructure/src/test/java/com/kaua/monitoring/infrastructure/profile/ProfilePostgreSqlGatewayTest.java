package com.kaua.monitoring.infrastructure.profile;

import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.VersionAccountType;
import com.kaua.monitoring.infrastructure.PostgreSqlGatewayTest;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@PostgreSqlGatewayTest
public class ProfilePostgreSqlGatewayTest {

    @Autowired
    private ProfilePostgreSqlGateway profileGateway;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void givenAnValidValues_whenCallsCreate_shouldReturnProfile() {
        final var expectedUserId = "123";
        final String expectedAvatarUrl = null;
        final var expectedVersionType = VersionAccountType.FREE;

        final var aProfile = Profile.newProfile(expectedUserId, expectedAvatarUrl);

        Assertions.assertEquals(0, profileRepository.count());

        final var actualProfile = profileGateway.create(aProfile);

        Assertions.assertEquals(1, profileRepository.count());

        Assertions.assertNotNull(actualProfile);
        Assertions.assertEquals(aProfile.getId().getValue(), actualProfile.getId().getValue());
        Assertions.assertEquals(expectedUserId, actualProfile.getUserId());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.getAvatarUrl());
        Assertions.assertEquals(expectedVersionType, actualProfile.getType());

        final var actualProfileEntity = profileRepository.findById(aProfile.getId().getValue()).get();

        Assertions.assertNotNull(actualProfileEntity);
        Assertions.assertEquals(aProfile.getId().getValue(), actualProfileEntity.getId());
        Assertions.assertEquals(expectedUserId, actualProfileEntity.getUserId());
        Assertions.assertEquals(expectedAvatarUrl, actualProfileEntity.getAvatarUrl());
        Assertions.assertEquals(expectedVersionType, actualProfileEntity.getType());
    }
}
