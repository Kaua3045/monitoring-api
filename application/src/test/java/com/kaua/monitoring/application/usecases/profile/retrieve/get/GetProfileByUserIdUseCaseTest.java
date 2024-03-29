package com.kaua.monitoring.application.usecases.profile.retrieve.get;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.VersionAccountType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetProfileByUserIdUseCaseTest {

    @InjectMocks
    private DefaultGetProfileByUserIdUseCase useCase;

    @Mock
    private ProfileGateway profileGateway;

    @Test
    public void givenAnValidProfileId_whenCallsGetById_shouldReturnProfile() {
        final var expectedProfileId = "123";
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedPassword = "12345678";
        final var expectedAvatarUrl = "imaginaria";
        final var expectedVersionType = VersionAccountType.FREE;

        final var aProfile = Profile.newProfile(
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl
        );

        when(profileGateway.findById(any()))
                .thenReturn(Optional.of(aProfile));

        final var aCommand = new GetProfileCommand(expectedProfileId);

        final var actualProfile = useCase.execute(aCommand);

        Assertions.assertNotNull(actualProfile);
        Assertions.assertNotNull(actualProfile.profileId());
        Assertions.assertEquals(expectedUsername, actualProfile.username());
        Assertions.assertEquals(expectedEmail, actualProfile.email());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.avatarUrl());
        Assertions.assertEquals(expectedVersionType.name(), actualProfile.type());

        Mockito.verify(profileGateway, times(1)).findById(aCommand.profileId());
    }

    @Test
    public void givenAnInvalidProfileId_whenCallsGetById_shouldThrowNotFoundException() {
        final var expectedUserId = "123";
        final var expectedErrorMessage = "Profile with ID 123 was not found";

        final var aCommand = new GetProfileCommand(expectedUserId);

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(profileGateway, times(1)).findById(aCommand.profileId());
    }
}
