package com.kaua.monitoring.infrastructure.profile;

import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.VersionAccountType;
import com.kaua.monitoring.infrastructure.PostgreSqlGatewayTest;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaFactory;
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
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedPassword = "123456789";
        final String expectedAvatarUrl = null;
        final var expectedVersionType = VersionAccountType.FREE;

        final var aProfile = Profile.newProfile(
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl);

        Assertions.assertEquals(0, profileRepository.count());

        final var actualProfile = profileGateway.create(aProfile);

        Assertions.assertEquals(1, profileRepository.count());

        Assertions.assertNotNull(actualProfile);
        Assertions.assertEquals(aProfile.getId().getValue(), actualProfile.getId().getValue());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.getAvatarUrl());
        Assertions.assertEquals(expectedVersionType, actualProfile.getType());

        final var actualProfileEntity = profileRepository.findById(aProfile.getId().getValue()).get();

        Assertions.assertNotNull(actualProfileEntity);
        Assertions.assertEquals(aProfile.getId().getValue(), actualProfileEntity.getId());
        Assertions.assertEquals(expectedAvatarUrl, actualProfileEntity.getAvatarUrl());
        Assertions.assertEquals(expectedPassword, actualProfileEntity.getPassword());
        Assertions.assertEquals(expectedVersionType, actualProfileEntity.getType());
    }

    @Test
    public void givenAnValidUserId_whenCallsFindByUserId_shouldReturnProfile() {
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedPassword = "12345678";
        final var expectedAvatarUrl = "url.com";
        final var expectedVersionType = VersionAccountType.FREE;

        final var aProfile = Profile.newProfile(
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl);

        Assertions.assertEquals(0, profileRepository.count());

        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        Assertions.assertEquals(1, profileRepository.count());

        final var actualProfile = profileGateway.findById(aProfile.getId().getValue()).get();

        Assertions.assertNotNull(actualProfile);
        Assertions.assertEquals(aProfile.getId().getValue(), actualProfile.getId().getValue());
        Assertions.assertEquals(expectedUsername, actualProfile.getUsername());
        Assertions.assertEquals(expectedEmail, actualProfile.getEmail());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.getAvatarUrl());
        Assertions.assertEquals(expectedVersionType, actualProfile.getType());
    }

    @Test
    public void givenAnValidId_whenCallsDeleteById_shouldBeOk() {
        final var aProfile = Profile.newProfile(
                "12345676",
                "a",
                "a@mail.com",
                null
        );
        final var expectedProfileId = aProfile.getId().getValue();

        Assertions.assertEquals(0, profileRepository.count());
        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));
        Assertions.assertEquals(1, profileRepository.count());

        Assertions.assertDoesNotThrow(() -> profileGateway.deleteById(expectedProfileId));
    }

    @Test
    public void givenAnValidValues_whenCallsUpdate_shouldReturnProfileUpdated() {
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedPassword = "12345678";
        final String expectedAvatarUrl = "imaginaria";
        final var expectedVersionType = VersionAccountType.PREMIUM;

        final var aProfile = Profile.newProfile(
                "ka",
                expectedEmail,
                expectedPassword,
                null);

        Assertions.assertEquals(0, profileRepository.count());

        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        Assertions.assertEquals(1, profileRepository.count());

        final var actualProfile = profileGateway.update(Profile.with(
                aProfile.getId(),
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl,
                expectedVersionType
        ));

        Assertions.assertNotNull(actualProfile);
        Assertions.assertEquals(aProfile.getId().getValue(), actualProfile.getId().getValue());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.getAvatarUrl());
        Assertions.assertEquals(expectedVersionType, actualProfile.getType());
    }
}
