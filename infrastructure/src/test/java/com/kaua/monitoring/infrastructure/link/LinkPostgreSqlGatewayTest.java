package com.kaua.monitoring.infrastructure.link;

import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.domain.pagination.SearchQuery;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.infrastructure.PostgreSqlGatewayTest;
import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaFactory;
import com.kaua.monitoring.infrastructure.link.persistence.LinkRepository;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaFactory;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@PostgreSqlGatewayTest
public class LinkPostgreSqlGatewayTest {

    @Autowired
    private LinkPostgreSqlGateway linkGateway;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void givenAnValidValues_whenCallsCreate_shouldReturnLinkId() {
        final var expectedProfile = Profile
                .newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );
        profileRepository.save(ProfileJpaFactory.toEntity(expectedProfile));

        final var expectedTitle = "teste";
        final var expectedUrl = "https://localhost.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS);

        final var expectedRepeat = LinkExecutions.NO_REPEAT;

        final var aLink = Link.newLink(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat,
                expectedProfile
        );

        Assertions.assertEquals(0, linkRepository.count());

        final var actualLink = linkGateway.create(aLink);

        Assertions.assertEquals(1, linkRepository.count());

        Assertions.assertEquals(aLink.getId().getValue(), actualLink.getId().getValue());
        Assertions.assertEquals(aLink.getTitle(), actualLink.getTitle());
        Assertions.assertEquals(aLink.getUrl(), actualLink.getUrl());
        Assertions.assertEquals(aLink.getExecuteDate(), actualLink.getExecuteDate());
        Assertions.assertEquals(aLink.getLinkExecution(), actualLink.getLinkExecution());
        Assertions.assertEquals(aLink.getProfile().getId().getValue(), actualLink.getProfile().getId().getValue());

        final var actualEntity = linkRepository.findById(aLink.getId().getValue()).get();

        Assertions.assertEquals(aLink.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aLink.getTitle(), actualEntity.getTitle());
        Assertions.assertEquals(aLink.getUrl(), actualEntity.getUrl());
        Assertions.assertEquals(aLink.getExecuteDate(), actualEntity.getExecuteDate());
        Assertions.assertEquals(aLink.getLinkExecution(), actualEntity.getLinkExecution());
        Assertions.assertEquals(aLink.getProfile().getId().getValue(), actualEntity.getProfile().getId());
    }

    @Test
    public void givenAnValidId_whenCallsFindById_shouldReturnLink() {
        final var expectedProfile = Profile
                .newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );
        profileRepository.save(ProfileJpaFactory.toEntity(expectedProfile));

        final var expectedTitle = "teste";
        final var expectedUrl = "https://localhost.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS);

        final var expectedRepeat = LinkExecutions.ON_SPECIFIC_DAY;

        final var aLink = Link.newLink(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat,
                expectedProfile
        );

        Assertions.assertEquals(0, linkRepository.count());

        linkRepository.save(LinkJpaFactory.toEntity(aLink));

        Assertions.assertEquals(1, linkRepository.count());

        final var actualLink = linkGateway.findById(aLink.getId().getValue()).get();

        Assertions.assertEquals(aLink.getId().getValue(), actualLink.getId().getValue());
        Assertions.assertEquals(aLink.getTitle(), actualLink.getTitle());
        Assertions.assertEquals(aLink.getUrl(), actualLink.getUrl());
        Assertions.assertEquals(aLink.getExecuteDate(), actualLink.getExecuteDate());
        Assertions.assertEquals(aLink.getLinkExecution(), actualLink.getLinkExecution());
        Assertions.assertEquals(aLink.getProfile().getId().getValue(), actualLink.getProfile().getId().getValue());
    }

    @Test
    public void givenAnValidId_whenCallsDeleteById_shouldBeOk() {
        final var expectedProfile = Profile
                .newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );
        profileRepository.save(ProfileJpaFactory.toEntity(expectedProfile));

        final var expectedTitle = "teste";
        final var expectedUrl = "https://localhost.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS);

        final var expectedRepeat = LinkExecutions.TWO_TIMES_A_MONTH;

        final var aLink = Link.newLink(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat,
                expectedProfile
        );

        Assertions.assertEquals(0, linkRepository.count());

        linkRepository.save(LinkJpaFactory.toEntity(aLink));

        Assertions.assertEquals(1, linkRepository.count());

        Assertions.assertDoesNotThrow(() -> linkGateway.deleteById(aLink.getId().getValue()));
    }

    @Test
    public void givenAnValidId_whenCallsFindAllByProfileId_shouldReturnEmptyList() {
        final var expectedProfile = Profile
                .newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );
        profileRepository.save(ProfileJpaFactory.toEntity(expectedProfile));

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualPage = linkGateway.findAllByProfileId(expectedProfile.getId().getValue(), aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "title,asc,0,10,4,4,Encurtador",
            "title,desc,0,10,4,4,Teste",
            "url,asc,0,10,4,4,Encurtador",
            "url,desc,0,10,4,4,Teste"
    })
    public void givenAnValidSortAndDirection_whenCallsFindAllByProfileId_shouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedTitle
    ) {
        final var expectedProfile = Profile
                .newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );
        profileRepository.save(ProfileJpaFactory.toEntity(expectedProfile));

        makeLinks(expectedProfile);
        final var expectedTerms = "";

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualPage = linkGateway.findAllByProfileId(expectedProfile.getId().getValue(), aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedTitle, actualPage.items().get(0).getTitle());
    }

    private void makeLinks(final Profile aProfile) {
        linkRepository.saveAll(List.of(
                LinkJpaFactory.toEntity(Link.newLink(
                        "Encurtador",
                        "https://curto.io",
                        Instant.now().plus(5, ChronoUnit.HOURS),
                        LinkExecutions.NO_REPEAT,
                        aProfile
                )),
                LinkJpaFactory.toEntity(Link.newLink(
                        "Teste",
                        "https://teste.com",
                        Instant.now().plus(5, ChronoUnit.HOURS),
                        LinkExecutions.ON_SPECIFIC_DAY,
                        aProfile
                )),
                LinkJpaFactory.toEntity(Link.newLink(
                        "Local",
                        "https://localhost.com",
                        Instant.now().plus(5, ChronoUnit.HOURS),
                        LinkExecutions.ON_SPECIFIC_DAY,
                        aProfile
                )),
                LinkJpaFactory.toEntity(Link.newLink(
                        "Famoso",
                        "https://famoso.com",
                        Instant.now().plus(5, ChronoUnit.HOURS),
                        LinkExecutions.ON_SPECIFIC_DAY,
                        aProfile
                ))
        ));
    }
}
