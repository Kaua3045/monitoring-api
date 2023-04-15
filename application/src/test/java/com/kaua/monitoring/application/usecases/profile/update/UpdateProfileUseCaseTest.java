package com.kaua.monitoring.application.usecases.profile.update;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.AvatarGateway;
import com.kaua.monitoring.application.gateways.EncrypterGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.Resource;
import com.kaua.monitoring.domain.profile.VersionAccountType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateProfileUseCaseTest {

    @InjectMocks
    private DefaultUpdateProfileUseCase useCase;

    @Mock
    private ProfileGateway profileGateway;

    @Mock
    private EncrypterGateway encrypterGateway;

    @Mock
    private AvatarGateway avatarGateway;

    @Test
    public void givenAnValidValues_whenCallsUpdate_shouldReturnProfileUpdated() {
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedPassword = "12345678";
        final Resource expectedAvatarUrl = null;
        final var expectedVersionType = VersionAccountType.PREMIUM;

        final var aProfile = Profile.newProfile(
                "aa",
                expectedEmail,
                "01456789023q",
                null
        );

        final var expectedProfileId = aProfile.getId().getValue();

        when(profileGateway.findById(expectedProfileId))
                .thenReturn(Optional.of(aProfile));

        when(encrypterGateway.encrypt(any()))
                .thenAnswer(returnsFirstArg());

        when(profileGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = new UpdateProfileCommand(
                expectedProfileId,
                expectedUsername,
                expectedPassword,
                expectedAvatarUrl,
                expectedVersionType.name()
        );

        final var actualProfile = Assertions.assertDoesNotThrow(
                () -> useCase.execute(aCommand).getRight());

        Assertions.assertNotNull(actualProfile);
        Assertions.assertEquals(expectedProfileId, actualProfile.profileId());
        Assertions.assertEquals(expectedUsername, actualProfile.username());
        Assertions.assertEquals(expectedEmail, actualProfile.email());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.avatarUrl());
        Assertions.assertEquals(expectedVersionType.name(), actualProfile.type());

        Mockito.verify(profileGateway, times(1)).findById(expectedProfileId);
        Mockito.verify(profileGateway, times(1)).update(any());
    }

    @Test
    public void givenAnInvalidProfileId_whenCallsUpdate_shouldThrowNotFoundException() {
        final var expectedProfileId = "123";
        final var expectedErrorMessage = "Profile with ID 123 was not found";

        when(profileGateway.findById(expectedProfileId))
                .thenReturn(Optional.empty());

        final var aCommand = new UpdateProfileCommand(
                expectedProfileId,
                "a",
                null,
                null,
                VersionAccountType.FREE.name()
        );

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand).getRight());

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(profileGateway, times(1)).findById(expectedProfileId);
        Mockito.verify(profileGateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidValues_whenCallsUpdate_shouldReturnOldProfileInfos() {
        final String expectedUsername = null;
        final var expectedEmail = "kaua@teste.com";
        final String expectedPassword = null;
        final Resource expectedAvatarUrl = null;
        final String expectedVersionType = null;

        final var aProfile = Profile.newProfile(
                "a",
                expectedEmail,
                "123456789",
                null
        );

        final var expectedProfileId = aProfile.getId().getValue();

        when(profileGateway.findById(any()))
                .thenReturn(Optional.of(aProfile));

        when(profileGateway.update(aProfile))
                .thenAnswer(returnsFirstArg());

        final var aCommand = new UpdateProfileCommand(
                expectedProfileId,
                expectedUsername,
                expectedPassword,
                expectedAvatarUrl,
                expectedVersionType
        );

        final var actualOutput = useCase.execute(aCommand).getRight();

        Assertions.assertEquals(aProfile.getId().getValue(), actualOutput.profileId());
        Assertions.assertEquals(aProfile.getUsername(), actualOutput.username());
        Assertions.assertEquals(aProfile.getEmail(), actualOutput.email());
        Assertions.assertEquals(aProfile.getType().name(), actualOutput.type());
        Assertions.assertEquals(aProfile.getAvatarUrl(), actualOutput.avatarUrl());

        Mockito.verify(profileGateway, times(1)).findById(expectedProfileId);
        Mockito.verify(profileGateway, times(1)).update(aProfile);
    }
}
