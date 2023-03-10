package com.kaua.monitoring.application.usecases.link.update;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
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
public class UpdateLinkUseCaseTest {

    @InjectMocks
    private DefaultUpdateLinkUseCase useCase;

    @Mock
    private LinkGateway linkGateway;

    @Test
    public void givenAnValidValues_whenCallsUpdate_shouldReturnLinkId() {
        final var expectedTitle = "teste";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = Instant.now().plus(10, ChronoUnit.DAYS);
        final var expectedRepeat = LinkExecutions.EVERY_DAYS.name();
        final var expectedProfile = Profile
                .newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );

        final var aLink = Link.newLink(
                "a",
                "https://localhost.com",
                Instant.now().plus(5, ChronoUnit.DAYS),
                LinkExecutions.TWO_TIMES_A_MONTH,
                expectedProfile
        );

        final var expectedId = aLink.getId().getValue();

        when(linkGateway.findById(any()))
                .thenReturn(Optional.of(aLink));

        when(linkGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = new UpdateLinkCommand(
                expectedId,
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat
        );

        final var actualLink = useCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualLink);
        Assertions.assertEquals(expectedId, actualLink.id());

        Mockito.verify(linkGateway, times(1)).findById(any());

        Mockito.verify(linkGateway, times(1)).update(argThat(link ->
                Objects.equals(expectedId, link.getId().getValue())
                && Objects.equals(expectedTitle, link.getTitle())
                && Objects.equals(expectedUrl, link.getUrl())
                && Objects.equals(expectedExecuteDate, link.getExecuteDate())
                && Objects.equals(expectedRepeat, link.getLinkExecution().name())
                && Objects.equals(expectedProfile, link.getProfile())
        ));
    }

    @Test
    public void givenAnInvalidValues_whenCallsUpdate_shouldReturnDomainErrors() {
        final String expectedTitle = null;
        final var expectedUrl = "";
        final Instant expectedExecuteDate = null;
        final var expectedRepeat = LinkExecutions.EVERY_DAYS.name();
        final var expectedProfile = Profile
                .newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );

        final var aLink = Link.newLink(
                "a",
                "https://localhost.com",
                Instant.now().plus(5, ChronoUnit.DAYS),
                LinkExecutions.NO_REPEAT,
                expectedProfile
        );

        final var expectedId = aLink.getId().getValue();
        final var expectedErrorsMessages = List.of(
                new Error("'title' should not be null or empty"),
                new Error("'url' you must provide a valid url"),
                new Error("'executeDate' should not be null")
        );

        when(linkGateway.findById(any()))
                .thenReturn(Optional.of(aLink));

        final var aCommand = new UpdateLinkCommand(
                expectedId,
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat
        );

        final var actualExceptions = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorsMessages, actualExceptions.getErrors());

        Mockito.verify(linkGateway, times(1)).findById(any());
        Mockito.verify(linkGateway, times(0)).update(any());
    }

    @Test
    public void givenAnValidValuesAndInvalidId_whenCallsUpdate_shouldThrowNotFoundException() {
        final var expectedTitle = "teste";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS);
        final var expectedRepeat = LinkExecutions.ON_SPECIFIC_DAY.name();
        final var expectedId = "123";
        final var expectedErrorMessage = "Link with ID 123 was not found";

        when(linkGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var aCommand = new UpdateLinkCommand(
                expectedId,
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat
        );

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand).getLeft()
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(linkGateway, times(1)).findById(any());
        Mockito.verify(linkGateway, times(0)).update(any());
    }
}
