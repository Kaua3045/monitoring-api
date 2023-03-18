package com.kaua.monitoring.infrastructure.application.link.retrieve.list;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.usecases.link.retrieve.list.profileId.ListLinkByProfileIdCommand;
import com.kaua.monitoring.application.usecases.link.retrieve.list.profileId.ListLinkByProfileIdUseCase;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.domain.pagination.SearchQuery;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.infrastructure.IntegrationTest;
import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaFactory;
import com.kaua.monitoring.infrastructure.link.persistence.LinkRepository;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaFactory;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@IntegrationTest
public class ListLinkByProfileIdUseCaseIT {

    @Autowired
    private ListLinkByProfileIdUseCase listLinkByProfileIdUseCase;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @SpyBean
    private LinkGateway linkGateway;

    @Test
    public void givenAnPrePersistedLinksAndValidCommand_whenCallsListLinkByProfileId_shouldReturnLinks() {
        final var aProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        final var aLink = Link.newLink(
                "teste",
                "https://local.com",
                Instant.now().plus(5, ChronoUnit.DAYS),
                LinkExecutions.NO_REPEAT,
                aProfile
        );

        linkRepository.save(LinkJpaFactory.toEntity(aLink));

        final var expectedProfileId = aProfile.getId().getValue();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "t";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        Assertions.assertEquals(1, linkRepository.count());

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var aCommand = new ListLinkByProfileIdCommand(
                expectedProfileId,
                aQuery
        );

        final var actualOutput = listLinkByProfileIdUseCase.execute(aCommand);

        Assertions.assertEquals(expectedItemsCount, actualOutput.items().size());
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());

        Mockito.verify(linkGateway, Mockito.times(1))
                .findAllByProfileId(Mockito.any(), Mockito.any());
    }

    @Test
    public void givenAnValidCommand_whenCallsListLinkByProfileId_shouldReturnEmptyList() {
        final var aProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        final var expectedProfileId = aProfile.getId().getValue();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "t";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, linkRepository.count());

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var aCommand = new ListLinkByProfileIdCommand(
                expectedProfileId,
                aQuery
        );

        final var actualOutput = listLinkByProfileIdUseCase.execute(aCommand);

        Assertions.assertEquals(expectedItemsCount, actualOutput.items().size());
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());

        Mockito.verify(linkGateway, Mockito.times(1))
                .findAllByProfileId(Mockito.any(), Mockito.any());
    }

    @Test
    public void givenAnInvalidProfileId_whenCallsListLinkByProfileId_shouldReturnEmptyList() {
        final var expectedProfileId = "123";
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "t";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Profile with ID 123 was not found";

        Assertions.assertEquals(0, linkRepository.count());

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var aCommand = new ListLinkByProfileIdCommand(
                expectedProfileId,
                aQuery
        );

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> listLinkByProfileIdUseCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(linkGateway, Mockito.times(0))
                .findAllByProfileId(Mockito.any(), Mockito.any());
    }
}
