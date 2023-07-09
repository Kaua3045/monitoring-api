package com.kaua.monitoring.application.usecases.profile.retrieve.get.me;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.JwtGateway;
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
public class MeProfileUseCaseTest {

    @InjectMocks
    private DefaultMeProfileUseCase useCase;

    @Mock
    private ProfileGateway profileGateway;

    @Mock
    private JwtGateway jwtGateway;

    @Test
    public void givenAnValidTokenAndSubIsProfileIdValid_whenCallsMeProfile_shouldReturnProfile() {
        final var expectedToken = "a1ab23";
        final var expectedTokenResult = "123";
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

        when(jwtGateway.extractTokenSubject(any()))
                .thenReturn(expectedTokenResult);

        when(profileGateway.findById(any()))
                .thenReturn(Optional.of(aProfile));

        final var aCommand = new MeProfileCommand(expectedToken);

        final var actualProfile = useCase.execute(aCommand);

        Assertions.assertNotNull(actualProfile);
        Assertions.assertNotNull(actualProfile.profileId());
        Assertions.assertEquals(expectedUsername, actualProfile.username());
        Assertions.assertEquals(expectedEmail, actualProfile.email());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.avatarUrl());
        Assertions.assertEquals(expectedVersionType.name(), actualProfile.type());

        Mockito.verify(jwtGateway, times(1)).extractTokenSubject(expectedToken);
        Mockito.verify(profileGateway, times(1)).findById(expectedTokenResult);
    }

    @Test
    public void givenAnInvalidToken_whenCallsMeProfile_shouldThrowNotFoundException() {
        final var expectedToken = "a123bsa";
        final var expectedErrorMessage = "Profile with ID null was not found";

        final var aCommand = new MeProfileCommand(expectedToken);

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(jwtGateway, times(1)).extractTokenSubject(any());
        Mockito.verify(profileGateway, times(1)).findById(any());
    }
}
