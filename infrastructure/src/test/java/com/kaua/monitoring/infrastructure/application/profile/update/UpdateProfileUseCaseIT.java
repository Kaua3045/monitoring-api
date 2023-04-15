package com.kaua.monitoring.infrastructure.application.profile.update;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.update.UpdateProfileCommand;
import com.kaua.monitoring.application.usecases.profile.update.UpdateProfileUseCase;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.Resource;
import com.kaua.monitoring.domain.profile.VersionAccountType;
import com.kaua.monitoring.infrastructure.IntegrationTest;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaFactory;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class UpdateProfileUseCaseIT {

    @Autowired
    private UpdateProfileUseCase updateProfileUseCase;

    @Autowired
    private ProfileRepository profileRepository;

    @SpyBean
    private ProfileGateway profileGateway;

    @Test
    public void givenAnValidCommand_whenCallsUpdateProfile_shouldReturnProfile() {
        final var aProfile = Profile.newProfile(
                "123",
                "zk",
                "kaua@teste.com",
                null
        );
        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        final var expectedId = aProfile.getId().getValue();
        final var expectedUsername = "kaua";
        final var expectedPassword = "12345678";
        final Resource expectedAvatarUrl = null;
        final var expectedType = VersionAccountType.PREMIUM;

        Assertions.assertEquals(1, profileRepository.count());

        final var aCommand = new UpdateProfileCommand(
                expectedId,
                expectedUsername,
                expectedPassword,
                expectedAvatarUrl,
                expectedType.name()
        );

        final var actualOutput = updateProfileUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.profileId());
        Assertions.assertEquals(expectedUsername, actualOutput.username());
        Assertions.assertEquals(aProfile.getEmail(), actualOutput.email());
        Assertions.assertEquals(expectedAvatarUrl, actualOutput.avatarUrl());
        Assertions.assertEquals(expectedType.name(), actualOutput.type());

        Mockito.verify(profileGateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(profileGateway, Mockito.times(1)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsUpdateProfile_shouldReturnDomainException() {
        final var aProfile = Profile.newProfile(
                "123",
                "zk",
                "kaua@teste.com",
                null
        );
        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        final var expectedId = aProfile.getId().getValue();
        final String expectedUsername = null;
        final String expectedPassword = " ";
        final Resource expectedAvatarUrl = null;
        final var expectedType = VersionAccountType.PREMIUM;

        Assertions.assertEquals(1, profileRepository.count());

        final var aCommand = new UpdateProfileCommand(
                expectedId,
                expectedUsername,
                expectedPassword,
                expectedAvatarUrl,
                expectedType.name()
        );

        final var actualOutput = updateProfileUseCase.execute(aCommand).getRight();

        Assertions.assertEquals(aProfile.getId().getValue(), actualOutput.profileId());
        Assertions.assertEquals(aProfile.getUsername(), actualOutput.username());
        Assertions.assertEquals(aProfile.getEmail(), actualOutput.email());
        Assertions.assertEquals(expectedType.name(), actualOutput.type());
        Assertions.assertEquals(aProfile.getAvatarUrl(), actualOutput.avatarUrl());

        Mockito.verify(profileGateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(profileGateway, Mockito.times(1)).update(Mockito.any());
    }

    @Test
    public void givenAnValidCommandAndInvalidId_whenCallsUpdateProfile_shouldReturnNotFoundException() {
        final var expectedId = "123";
        final var expectedUsername = "kaua";
        final var expectedPassword = "12345678";
        final Resource expectedAvatarUrl = null;
        final var expectedType = VersionAccountType.PREMIUM;

        final var expectedErrorMessage = "Profile with ID 123 was not found";

        Assertions.assertEquals(0, profileRepository.count());

        final var aCommand = new UpdateProfileCommand(
                expectedId,
                expectedUsername,
                expectedPassword,
                expectedAvatarUrl,
                expectedType.name()
        );

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> updateProfileUseCase.execute(aCommand).getLeft()
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(profileGateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(profileGateway, Mockito.times(0)).update(Mockito.any());
    }
}
