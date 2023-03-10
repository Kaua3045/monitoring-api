package com.kaua.monitoring.application.usecases.link.retrieve.list.profileId;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListLinkByProfileIdUseCaseTest {

    @InjectMocks
    private DefaultListLinkByProfileIdUseCase useCase;

    @Mock
    private LinkGateway linkGateway;

    @Mock
    private ProfileGateway profileGateway;

    @Test
    public void givenAnValidProfileIdAndPrePersistedLinks_whenCallsFindAllByProfileId_shouldReturnLinks() {
        final var expectedProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        final var expectedLinks = List.of(
                Link.newLink(
                        "local",
                        "https://localhost.com",
                        Instant.now().plus(5, ChronoUnit.DAYS),
                        LinkExecutions.NO_REPEAT,
                        expectedProfile
                ),
                Link.newLink(
                        "teste 2",
                        "https://localhost.com",
                        Instant.now().plus(5, ChronoUnit.DAYS),
                        LinkExecutions.TWO_TIMES_A_MONTH,
                        expectedProfile
                )
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "T";
        final var expectedSort = "executeDate";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = expectedLinks.stream()
                        .map(LinkOutput::from)
                                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                expectedLinks
        );

        when(profileGateway.findById(any()))
                .thenReturn(Optional.of(expectedProfile));

        when(linkGateway.findAllByProfileId(any(), any()))
                .thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var aCommand = new ListLinkByProfileIdCommand(
                expectedProfile.getId().getValue(),
                aQuery
        );

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(profileGateway, times(1)).findById(aCommand.profileId());
        Mockito.verify(linkGateway, times(1))
                .findAllByProfileId(aCommand.profileId(), aQuery);
    }

    @Test
    public void givenAnValidProfileId_whenCallsFindAllByProfileId_shouldReturnLinks() {
        final var expectedProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        final var expectedLinks = List.<Link>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "T";
        final var expectedSort = "executeDate";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = expectedLinks.stream()
                .map(LinkOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                expectedLinks
        );

        when(profileGateway.findById(any()))
                .thenReturn(Optional.of(expectedProfile));

        when(linkGateway.findAllByProfileId(any(), any()))
                .thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var aCommand = new ListLinkByProfileIdCommand(
                expectedProfile.getId().getValue(),
                aQuery
        );

        final var actualLinks = useCase.execute(aCommand);

        Assertions.assertEquals(expectedItems.size(), actualLinks.items().size());

        Mockito.verify(profileGateway, times(1)).findById(aCommand.profileId());
        Mockito.verify(linkGateway, times(1)).findAllByProfileId(any(), any());
    }

    @Test
    public void givenAnInvalidProfileId_whenCallsFindAllByProfileId_shouldThrowNotFoundException() {
        final var expectedProfile = "123";
        final var expectedErrorMessage = "Profile with ID 123 was not found";

        when(profileGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var aCommand = new ListLinkByProfileIdCommand(
                expectedProfile,
                new SearchQuery(
                        0,
                        10,
                        "T",
                        "executeDate",
                        "asc"
                ));

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
