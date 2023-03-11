package com.kaua.monitoring.infrastructure.checking;

import com.kaua.monitoring.domain.checking.LinkResponse;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.domain.pagination.SearchQuery;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.infrastructure.PostgreSqlGatewayTest;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseJpaFactory;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseRepository;
import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaFactory;
import com.kaua.monitoring.infrastructure.link.persistence.LinkRepository;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaFactory;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@PostgreSqlGatewayTest
public class LinkResponsePostgreSqlGatewayTest {

    @Autowired
    private LinkResponsePostgreSqlGateway linkResponseGateway;

    @Autowired
    private LinkResponseRepository linkResponseRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ProfileRepository profileRepository;


    @ParameterizedTest
    @CsvSource({
            "responseMessage,asc,0,10,2,2,NOT-FOUND",
            "responseMessage,desc,0,10,2,2,OK"
    })
    public void givenAnValidSortAndDirection_whenCallsFindAllByUrlId_shouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedresponseMessage
    ) {
        final var expectedProfile = Profile
                .newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );
        profileRepository.save(ProfileJpaFactory.toEntity(expectedProfile));
        final var expectedLink = Link
                .newLink(
                        "teste",
                        "https://teste.com",
                        Instant.now().plus(5, ChronoUnit.DAYS),
                        LinkExecutions.NO_REPEAT,
                        expectedProfile
                );
        linkRepository.save(LinkJpaFactory.toEntity(expectedLink));

        final var expectedLinkResponses = List.of(
                LinkResponseJpaFactory.toEntity(LinkResponse.newLinkResponse(
                        "OK",
                        200,
                        expectedLink
                )),
                LinkResponseJpaFactory.toEntity(LinkResponse.newLinkResponse(
                        "NOT-FOUND",
                        404,
                        expectedLink
                ))
        );
        linkResponseRepository.saveAllAndFlush(expectedLinkResponses);

        final var expectedTerms = "";

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualPage = linkResponseGateway
                .findAllByUrlId(expectedLink.getId().getValue(), aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedresponseMessage, actualPage.items().get(0).getResponseMessage());
    }
}
