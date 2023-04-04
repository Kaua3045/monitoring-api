package com.kaua.monitoring.infrastructure.application.checking.retrieve.list;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkResponseGateway;
import com.kaua.monitoring.application.usecases.checking.retrieve.ListLinkResponseByUrlIdCommand;
import com.kaua.monitoring.application.usecases.checking.retrieve.ListLinkResponseByUrlIdUseCase;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdCommand;
import com.kaua.monitoring.domain.checking.LinkResponse;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.infrastructure.IntegrationTest;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseJpaFactory;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseRepository;
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
public class ListLinkResponseByUrlIdUseCaseIT {

    @Autowired
    private ListLinkResponseByUrlIdUseCase listLinkResponseByUrlIdUseCase;

    @Autowired
    private LinkResponseRepository linkResponseRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private LinkRepository linkRepository;

    @SpyBean
    private LinkResponseGateway linkResponseGateway;

    @Test
    public void givenAnPrePersistedLinkResponsesAndValidCommand_whenCallsListLinkResponseByUrlId_shouldReturnLinkResponses() {
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

        final var aLinkResponse = LinkResponse.newLinkResponse(
                "OK",
                200,
                Instant.now(),
                150,
                aLink
        );

        linkResponseRepository.save(LinkResponseJpaFactory.toEntity(aLinkResponse));

        final var expectedUrlId = aLink.getId().getValue();
        final var expectedStartTimestamp = Instant.now().minus(10, ChronoUnit.DAYS);
        final var expectedEndTimestamp = Instant.now().plus(10, ChronoUnit.DAYS);

        Assertions.assertEquals(1, linkResponseRepository.count());

        final var aCommand = new ListLinkResponseByUrlIdCommand(
                expectedUrlId,
                expectedStartTimestamp,
                expectedEndTimestamp
        );

        final var actualOutput = listLinkResponseByUrlIdUseCase.execute(aCommand);

        Assertions.assertEquals(aLinkResponse.getId().getValue(), actualOutput.get(0).id());
        Assertions.assertEquals(aLinkResponse.getResponseMessage(), actualOutput.get(0).responseMessage());
        Assertions.assertEquals(aLinkResponse.getStatusCode(), actualOutput.get(0).statusCode());
        Assertions.assertNotNull(actualOutput.get(0).verifiedDate());
        Assertions.assertEquals(expectedUrlId, actualOutput.get(0).urlId());

        Mockito.verify(linkResponseGateway, Mockito.times(1))
                .findAllByUrlIdAndFilterByVerifiedDate(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void givenAnValidCommand_whenCallsListLinkResponseByUrlId_shouldReturnEmptyList() {
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

        final var expectedUrlId = aLink.getId().getValue();
        final var expectedStartTimestamp = Instant.now().minus(10, ChronoUnit.DAYS);
        final var expectedEndTimestamp = Instant.now().plus(10, ChronoUnit.DAYS);

        Assertions.assertEquals(0, linkResponseRepository.count());

        final var aCommand = new ListLinkResponseByUrlIdCommand(
                expectedUrlId,
                expectedStartTimestamp,
                expectedEndTimestamp
        );

        final var actualOutput = listLinkResponseByUrlIdUseCase.execute(aCommand);

        Assertions.assertEquals(0, actualOutput.size());

        Mockito.verify(linkResponseGateway, Mockito.times(1))
                .findAllByUrlIdAndFilterByVerifiedDate(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void givenAnInvalidLinkId_whenCallsLinkResponseByUrlId_shouldReturnNotFound() {
        final var expectedLinkId = "123";
        final var expectedStartTimestamp = Instant.now().minus(10, ChronoUnit.DAYS);
        final var expectedEndTimestamp = Instant.now().plus(10, ChronoUnit.DAYS);

        final var expectedErrorMessage = "Link with ID 123 was not found";

        Assertions.assertEquals(0, linkRepository.count());

        final var aCommand = new ListLinkResponseByUrlIdCommand(
                expectedLinkId,
                expectedStartTimestamp,
                expectedEndTimestamp
        );

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> listLinkResponseByUrlIdUseCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Assertions.assertEquals(0, linkRepository.count());

        Mockito.verify(linkResponseGateway, Mockito.times(0))
                .findAllByUrlIdAndFilterByVerifiedDate(Mockito.any(), Mockito.any(), Mockito.any());
    }
}
