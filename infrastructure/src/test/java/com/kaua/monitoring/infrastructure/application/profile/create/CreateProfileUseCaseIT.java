package com.kaua.monitoring.infrastructure.application.profile.create;

import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileCommand;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileUseCase;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.profile.VersionAccountType;
import com.kaua.monitoring.infrastructure.IntegrationTest;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

@IntegrationTest
public class CreateProfileUseCaseIT {

    @Autowired
    private CreateProfileUseCase createProfileUseCase;

    @Autowired
    private ProfileRepository profileRepository;

    @SpyBean
    private ProfileGateway profileGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateProfile_shouldReturnProfile() {
        final var expectedUserId = "123";
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final String expectedAvatarUrl = null;
        final var expectedType = VersionAccountType.FREE;

        Assertions.assertEquals(0, profileRepository.count());

        final var aCommand = new CreateProfileCommand(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl
        );

        final var actualOutput = createProfileUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.profileId());

        Assertions.assertEquals(1, profileRepository.count());

        final var actualProfile = profileRepository.findById(actualOutput.profileId()).get();

        Assertions.assertEquals(expectedUserId, actualProfile.getUserId());
        Assertions.assertEquals(expectedUsername, actualProfile.getUsername());
        Assertions.assertEquals(expectedEmail, actualProfile.getEmail());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.getAvatarUrl());
        Assertions.assertEquals(expectedType, actualProfile.getType());

        Mockito.verify(profileGateway, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateProfile_shouldReturnDomainException() {
        final var expectedUserId = " ";
        final String expectedUsername = null;
        final var expectedEmail = " ";
        final var expectedAvatarUrl = "url";

        final var expectedErrorsMessages = List.of(
                new Error("'userId' should not be null or empty"),
                new Error("'username' should not be null or empty"),
                new Error("'email' should not be null or empty")
        );

        Assertions.assertEquals(0, profileRepository.count());

        final var aCommand = new CreateProfileCommand(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl
        );

        final var actualException = createProfileUseCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorsMessages, actualException.getErrors());

        Assertions.assertEquals(0, profileRepository.count());

        Mockito.verify(profileGateway, Mockito.times(0)).create(Mockito.any());
    }
}
