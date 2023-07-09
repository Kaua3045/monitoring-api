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
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedPassword = "12345678";
        final String expectedAvatarUrl = null;
        final var expectedType = VersionAccountType.FREE;

        Assertions.assertEquals(0, profileRepository.count());

        final var aCommand = new CreateProfileCommand(
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl
        );

        final var actualOutput = createProfileUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.profileId());

        Assertions.assertEquals(1, profileRepository.count());

        final var actualProfile = profileRepository.findById(actualOutput.profileId()).get();

        Assertions.assertEquals(expectedUsername, actualProfile.getUsername());
        Assertions.assertEquals(expectedEmail, actualProfile.getEmail());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.getAvatarUrl());
        Assertions.assertEquals(expectedType, actualProfile.getType());

        Mockito.verify(profileGateway, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateProfile_shouldReturnDomainException() {
        final String expectedUsername = null;
        final var expectedEmail = " ";
        final var expectedPassword = " ";
        final var expectedAvatarUrl = "url";

        final var expectedErrorsMessages = List.of(
                new Error("'username' should not be null or empty"),
                new Error("'email' should not be null or empty"),
                new Error("'password' should not be null or empty"),
                new Error("'password' must be at least 8 characters")
        );

        Assertions.assertEquals(0, profileRepository.count());

        final var aCommand = new CreateProfileCommand(
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl
        );

        final var actualException = createProfileUseCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorsMessages, actualException.getErrors());

        Assertions.assertEquals(0, profileRepository.count());

        Mockito.verify(profileGateway, Mockito.times(0)).create(Mockito.any());
    }
}
