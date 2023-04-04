package com.kaua.monitoring.application.usecases.checking.retrieve;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.gateways.LinkResponseGateway;
import com.kaua.monitoring.domain.checking.LinkResponse;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListLinkResponseByUrlIdUseCaseTest {

    @InjectMocks
    private DefaultListLinkResponseByUrlIdUseCase useCase;

    @Mock
    private LinkResponseGateway linkResponseGateway;

    @Mock
    private LinkGateway linkGateway;

    @Test
    public void givenAnValidUrlIdAndPrePersistedLinkResponses_whenCallsFindAllTop90_shouldReturnLinkResponses() {
        final var expectedProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        final var expectedLink = Link.newLink(
                "local",
                "https://localhost.com",
                Instant.now().plus(5, ChronoUnit.DAYS),
                LinkExecutions.NO_REPEAT,
                expectedProfile
        );

        final var expectedLinkResponses = List.of(
                LinkResponse.newLinkResponse(
                        "OK",
                        200,
                        Instant.now(),
                        0,
                        expectedLink
                ),
                LinkResponse.newLinkResponse(
                        "OK",
                        200,
                        Instant.now(),
                        0,
                        expectedLink
                )
        );

        when(linkGateway.findById(any()))
                .thenReturn(Optional.of(expectedLink));

        when(linkResponseGateway.findAllByUrlIdAndFilterByVerifiedDate(any(), any(), any()))
                .thenReturn(expectedLinkResponses);

        final var aCommand = new ListLinkResponseByUrlIdCommand(
                expectedLink.getId().getValue(),
                Instant.now(),
                Instant.now().plus(5, ChronoUnit.MINUTES)
        );

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertEquals(expectedLinkResponses.size(), actualOutput.size());

        Mockito.verify(linkGateway, times(1)).findById(aCommand.urlId());
        Mockito.verify(linkResponseGateway, times(1))
                .findAllByUrlIdAndFilterByVerifiedDate(
                        aCommand.urlId(), aCommand.startTimestamp(), aCommand.endTimestamp());
    }

    @Test
    public void givenAnValidUrlId_whenCallsFindAllTop90_shouldReturnEmptyList() {
        final var expectedProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        final var expectedLink = Link.newLink(
                "local",
                "https://localhost.com",
                Instant.now().plus(5, ChronoUnit.DAYS),
                LinkExecutions.NO_REPEAT,
                expectedProfile
        );

        final var expectedLinkResponses = List.<LinkResponse>of();

        when(linkGateway.findById(any()))
                .thenReturn(Optional.of(expectedLink));

        when(linkResponseGateway.findAllByUrlIdAndFilterByVerifiedDate(any(), any(), any()))
                .thenReturn(expectedLinkResponses);

        final var aCommand = new ListLinkResponseByUrlIdCommand(
                expectedProfile.getId().getValue(),
                Instant.now(),
                Instant.now().plus(5, ChronoUnit.MINUTES)
        );

        final var actualLinks = useCase.execute(aCommand);

        Assertions.assertEquals(expectedLinkResponses.size(), actualLinks.size());

        Mockito.verify(linkGateway, times(1)).findById(aCommand.urlId());
        Mockito.verify(linkResponseGateway, times(1))
                .findAllByUrlIdAndFilterByVerifiedDate(any(), any(), any());
    }

    @Test
    public void givenAnInvalidUrlId_whenCallsFindAllByUrlId_shouldThrowNotFoundException() {
        final var expectedUrlId = "123";
        final var expectedErrorMessage = "Link with ID 123 was not found";

        when(linkGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var aCommand = new ListLinkResponseByUrlIdCommand(
                expectedUrlId,
                Instant.now(),
                Instant.now().plus(5, ChronoUnit.MINUTES)
        );

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
