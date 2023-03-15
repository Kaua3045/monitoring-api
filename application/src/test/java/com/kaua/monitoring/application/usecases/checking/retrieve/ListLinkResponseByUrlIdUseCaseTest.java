package com.kaua.monitoring.application.usecases.checking.retrieve;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.gateways.LinkResponseGateway;
import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import com.kaua.monitoring.domain.checking.LinkResponse;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.domain.pagination.SearchQuery;
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
    public void givenAnValidUrlIdAndPrePersistedLinkResponses_whenCallsFindAllByUrlId_shouldReturnLinkResponses() {
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
                        expectedLink
                ),
                LinkResponse.newLinkResponse(
                        "OK",
                        200,
                        expectedLink
                )
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "T";
        final var expectedSort = "executeDate";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = expectedLinkResponses
                .stream().map(LinkResponseOutput::from).toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                expectedLinkResponses
        );

        when(linkGateway.findById(any()))
                .thenReturn(Optional.of(expectedLink));

        when(linkResponseGateway.findAllByUrlId(any(), any()))
                .thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var aCommand = new ListLinkResponseByUrlIdCommand(
                expectedLink.getId().getValue(),
                aQuery
        );

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(linkGateway, times(1)).findById(aCommand.urlId());
        Mockito.verify(linkResponseGateway, times(1))
                .findAllByUrlId(aCommand.urlId(), aQuery);
    }

    @Test
    public void givenAnValidUrlId_whenCallsFindAllByUrlId_shouldReturnEmptyList() {
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

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "T";
        final var expectedSort = "executeDate";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                expectedLinkResponses
        );

        when(linkGateway.findById(any()))
                .thenReturn(Optional.of(expectedLink));

        when(linkResponseGateway.findAllByUrlId(any(), any()))
                .thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var aCommand = new ListLinkResponseByUrlIdCommand(
                expectedProfile.getId().getValue(),
                aQuery
        );

        final var actualLinks = useCase.execute(aCommand);

        Assertions.assertEquals(expectedLinkResponses.size(), actualLinks.items().size());

        Mockito.verify(linkGateway, times(1)).findById(aCommand.urlId());
        Mockito.verify(linkResponseGateway, times(1))
                .findAllByUrlId(any(), any());
    }

    @Test
    public void givenAnInvalidUrlId_whenCallsFindAllByUrlId_shouldThrowNotFoundException() {
        final var expectedUrlId = "123";
        final var expectedErrorMessage = "Link with ID 123 was not found";

        when(linkGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var aCommand = new ListLinkResponseByUrlIdCommand(
                expectedUrlId,
                new SearchQuery(
                        0,
                        10,
                        "T",
                        "statusCode",
                        "asc"
                ));

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
