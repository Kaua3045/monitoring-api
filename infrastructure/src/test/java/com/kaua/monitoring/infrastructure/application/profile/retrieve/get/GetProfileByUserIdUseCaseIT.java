package com.kaua.monitoring.infrastructure.application.profile.retrieve.get;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.GetProfileByUserIdUseCase;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.GetProfileCommand;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.infrastructure.IntegrationTest;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaFactory;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class GetProfileByUserIdUseCaseIT {

    @Autowired
    private GetProfileByUserIdUseCase getProfileByUserIdUseCase;

    @Autowired
    private ProfileRepository profileRepository;

    @SpyBean
    private ProfileGateway profileGateway;

    @Test
    public void givenAValidCommand_whenCallsFindByProfileId_shouldReturnProfile() {
        final var aProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        final var expectedUserId = aProfile.getId().getValue();

        Assertions.assertEquals(1, profileRepository.count());

        final var aCommand = new GetProfileCommand(expectedUserId);

        final var actualOutput = getProfileByUserIdUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(aProfile.getId().getValue(), actualOutput.profileId());
        Assertions.assertEquals(aProfile.getUsername(), actualOutput.username());
        Assertions.assertEquals(aProfile.getEmail(), actualOutput.email());
        Assertions.assertEquals(aProfile.getAvatarUrl(), actualOutput.avatarUrl());
        Assertions.assertEquals(aProfile.getType().name(), actualOutput.type());

        Mockito.verify(profileGateway, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsFindByUserId_shouldReturnNotFoundException() {
        final var expectedUserId = "123";
        final var expectedErrorMessage = "Profile with ID 123 was not found";

        Assertions.assertEquals(0, profileRepository.count());

        final var aCommand = new GetProfileCommand(expectedUserId);

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> getProfileByUserIdUseCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(profileGateway, Mockito.times(1)).findById(Mockito.any());
    }
}
