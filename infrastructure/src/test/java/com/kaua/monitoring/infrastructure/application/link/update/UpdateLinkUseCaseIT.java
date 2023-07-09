package com.kaua.monitoring.infrastructure.application.link.update;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.usecases.link.update.UpdateLinkCommand;
import com.kaua.monitoring.application.usecases.link.update.UpdateLinkUseCase;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
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
import java.util.List;

@IntegrationTest
public class UpdateLinkUseCaseIT {

    @Autowired
    private UpdateLinkUseCase updateLinkUseCase;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @SpyBean
    private LinkGateway linkGateway;

    @Test
    public void givenAnValidCommand_whenCallsUpdateLink_shouldReturnLinkID() {
        final var aProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        final var aLink = Link.newLink(
                "t",
                "https://local.com",
                Instant.now().plus(5, ChronoUnit.DAYS),
                Instant.now().plus(5, ChronoUnit.DAYS),
                LinkExecutions.NO_REPEAT,
                aProfile
        );

        linkRepository.save(LinkJpaFactory.toEntity(aLink));

        final var expectedId = aLink.getId().getValue();
        final var expectedTitle = "teste";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS);
        final var expectedLinkExecution = LinkExecutions.EVERY_DAYS;
        final var expectedProfileId = aProfile.getId().getValue();

        Assertions.assertEquals(1, linkRepository.count());

        final var aCommand = new UpdateLinkCommand(
                expectedId,
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution.name()
        );

        final var actualOutput = updateLinkUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Assertions.assertEquals(1, linkRepository.count());

        final var actualLink = linkRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(actualOutput.id(), actualLink.getId());
        Assertions.assertEquals(expectedTitle, actualLink.getTitle());
        Assertions.assertEquals(expectedUrl, actualLink.getUrl());
        Assertions.assertEquals(expectedExecuteDate, actualLink.getExecuteDate());
        Assertions.assertEquals(expectedLinkExecution, actualLink.getLinkExecution());
        Assertions.assertEquals(expectedProfileId, actualLink.getProfile().getId());

        Mockito.verify(linkGateway, Mockito.times(1)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsUpdateLink_shouldReturnDomainException() {
        final var aProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        final var aLink = Link.newLink(
                "t",
                "https://local.com",
                Instant.now().plus(5, ChronoUnit.DAYS),
                Instant.now().plus(5, ChronoUnit.DAYS),
                LinkExecutions.NO_REPEAT,
                aProfile
        );

        linkRepository.save(LinkJpaFactory.toEntity(aLink));

        final var expectedId = aLink.getId().getValue();
        final var expectedTitle = " ";
        final var expectedUrl = "teste";
        final var expectedExecuteDate = Instant.now().minus(15, ChronoUnit.DAYS);
        final var expectedLinkExecution = LinkExecutions.EVERY_DAYS;

        final var expectedErrorsMessages = List.of(
                new Error("'title' should not be null or empty"),
                new Error("'url' you must provide a valid url"),
                new Error("'executeDate' cannot be a date that has already passed")
        );

        Assertions.assertEquals(1, linkRepository.count());

        final var aCommand = new UpdateLinkCommand(
                expectedId,
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution.name()
        );

        final var actualExceptions = updateLinkUseCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorsMessages, actualExceptions.getErrors());

        Assertions.assertEquals(1, linkRepository.count());

        Mockito.verify(linkGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidLinkId_whenCallsUpdateLink_shouldReturnNotFound() {
        final var expectedTitle = "a";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS);
        final var expectedLinkExecution = LinkExecutions.EVERY_DAYS;
        final var expectedLinkId = "123";

        final var expectedErrorMessage = "Link with ID 123 was not found";

        Assertions.assertEquals(0, linkRepository.count());

        final var aCommand = new UpdateLinkCommand(
                expectedLinkId,
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution.name()
        );

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> updateLinkUseCase.execute(aCommand).getLeft()
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Assertions.assertEquals(0, linkRepository.count());

        Mockito.verify(linkGateway, Mockito.times(0)).update(Mockito.any());
    }
}
