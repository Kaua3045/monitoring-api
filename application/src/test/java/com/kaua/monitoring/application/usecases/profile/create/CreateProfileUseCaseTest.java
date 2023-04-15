package com.kaua.monitoring.application.usecases.profile.create;

import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.VersionAccountType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateProfileUseCaseTest {

    @InjectMocks
    private DefaultCreateProfileUseCase useCase;

    @Mock
    private ProfileGateway profileGateway;

    @Test
    public void givenAnValidValues_whenCallsCreate_shouldReturnProfile() {
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedPassword = "12345678";
        final var expectedAvatarUrl = "url/imaginaria";
        final var expectedType = VersionAccountType.FREE;

        when(profileGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = new CreateProfileCommand(
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl);

        final var actualProfile = useCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualProfile);
        Assertions.assertNotNull(actualProfile.profileId());
        Assertions.assertEquals(expectedUsername, actualProfile.username());
        Assertions.assertEquals(expectedEmail, actualProfile.email());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.avatarUrl());
        Assertions.assertEquals(expectedType.name(), actualProfile.type());
    }

    @Test
    public void givenAnInvalidValues_whenCallsCreate_shouldReturnDomainException() {
        final var expectedUsername = " ";
        final String expectedEmail = null;
        final String expectedPassword = null;
        final var expectedAvatarUrl = "url/imaginaria";

        final var expectedErrorsMessages = List.of(
                new Error("'username' should not be null or empty"),
                new Error("'email' should not be null or empty"),
                new Error("'password' should not be null or empty")
        );

        final var aCommand = new CreateProfileCommand(
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl);

        final var actualExceptions = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorsMessages, actualExceptions.getErrors());
    }

    @Test
    public void givenAnValidValuesAndExistsEmail_whenCallsCreate_shouldReturnDomainException() {
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedPassword = "12345678";
        final var expectedAvatarUrl = "url/imaginaria";

        final var expectedErrorMessage = "Email already exists";

        when(profileGateway.findByEmail(expectedEmail))
                .thenReturn(Optional.of(Profile.newProfile(
                        "123",
                        "a",
                        "kaua@teste.com",
                        null
                )));

        final var aCommand = new CreateProfileCommand(
                expectedUsername,
                expectedEmail,
                expectedPassword,
                expectedAvatarUrl);

        final var actualException = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
