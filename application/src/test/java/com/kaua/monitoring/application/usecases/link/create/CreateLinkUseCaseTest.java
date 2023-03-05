package com.kaua.monitoring.application.usecases.link.create;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.profile.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateLinkUseCaseTest {

    @InjectMocks
    private DefaultCreateLinkUseCase useCase;

    @Mock
    private LinkGateway linkGateway;

    @Mock
    private ProfileGateway profileGateway;

    @Test
    public void givenAnValidValues_whenCallsCreate_shouldReturnLinkId() {
        final var expectedTitle = "teste";
        final var expectedUrl = "https://localhost.com";
        final var expectedExecuteDate = Instant.now().plus(1, ChronoUnit.DAYS);
        final var expectedRepeat = true;
        final var expectedProfile = Profile.
                newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );

        when(profileGateway.findById(any()))
                .thenReturn(Optional.of(expectedProfile));

        when(linkGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = new CreateLinkCommand(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat,
                expectedProfile.getId().getValue()
        );

        final var aLink = useCase.execute(aCommand).getRight();

        Assertions.assertNotNull(aLink);
        Assertions.assertNotNull(aLink.id());

        Mockito.verify(profileGateway, times(1)).findById(any());

        Mockito.verify(linkGateway, times(1)).create(argThat(link ->
                Objects.nonNull(link.getId().getValue())
                && Objects.equals(expectedTitle, link.getTitle())
                && Objects.equals(expectedUrl, link.getUrl())
                && Objects.equals(expectedExecuteDate, link.getExecuteDate())
                && Objects.equals(expectedRepeat, link.isRepeat())
                && Objects.equals(expectedProfile, link.getProfile())
        ));
    }

    @Test
    public void givenAnInvalidValues_whenCallsCreate_shouldReturnDomainErrors() {
        final String expectedTitle = null;
        final var expectedUrl = "a";
        final var expectedExecuteDate = Instant.parse("2007-12-03T10:15:30.00Z");
        final var expectedRepeat = true;
        final var expectedProfile = Profile.
                newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );

        final var expectedErrorsMessages = List.of(
                new Error("'title' should not be null or empty"),
                new Error("'url' you must provide a valid url"),
                new Error("'executeDate' cannot be a date that has already passed")
        );

        when(profileGateway.findById(any()))
                .thenReturn(Optional.of(expectedProfile));

        final var aCommand = new CreateLinkCommand(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat,
                expectedProfile.getId().getValue()
        );

        final var actualExceptions = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorsMessages, actualExceptions.getErrors());

        Mockito.verify(profileGateway, times(1)).findById(any());
        Mockito.verify(linkGateway, times(0)).create(any());
    }

    @Test
    public void givenAnValidValuesAndInvalidProfile_whenCallsCreate_shouldThrowNotFoundException() {
        final var expectedTitle = "teste";
        final var expectedUrl = "https://localhost.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.MINUTES);
        final var expectedRepeat = true;
        final var expectedProfile = "123";

        final var expectedErrorMessage = "Profile with ID 123 was not found";

        when(profileGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var aCommand = new CreateLinkCommand(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat,
                expectedProfile
        );

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand).getLeft()
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(profileGateway, times(1)).findById(any());
        Mockito.verify(linkGateway, times(0)).create(any());
    }
}
