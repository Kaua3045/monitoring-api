package com.kaua.monitoring.infrastructure.application.link.create;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.usecases.link.create.CreateLinkCommand;
import com.kaua.monitoring.application.usecases.link.create.CreateLinkUseCase;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.infrastructure.IntegrationTest;
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
public class CreateLinkUseCaseIT {

    @Autowired
    private CreateLinkUseCase createLinkUseCase;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @SpyBean
    private LinkGateway linkGateway;

    @Test
    public void givenAnValidCommand_whenCallsCreateLink_shouldReturnLinkID() {
        final var aProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        final var expectedTitle = "teste";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS);
        final var expectedLinkExecution = LinkExecutions.EVERY_DAYS;
        final var expectedProfileId = aProfile.getId().getValue();

        Assertions.assertEquals(0, linkRepository.count());

        final var aCommand = new CreateLinkCommand(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution.name(),
                expectedProfileId
        );

        final var actualOutput = createLinkUseCase.execute(aCommand).getRight();

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

        Mockito.verify(linkGateway, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateLink_shouldReturnDomainException() {
        final var aProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        final var expectedTitle = " ";
        final var expectedUrl = "teste";
        final var expectedExecuteDate = Instant.now().minus(5, ChronoUnit.DAYS);
        final var expectedLinkExecution = LinkExecutions.EVERY_DAYS;
        final var expectedProfileId = aProfile.getId().getValue();

        final var expectedErrorsMessages = List.of(
                new Error("'title' should not be null or empty"),
                new Error("'url' you must provide a valid url"),
                new Error("'executeDate' cannot be a date that has already passed")
        );

        Assertions.assertEquals(0, linkRepository.count());

        final var aCommand = new CreateLinkCommand(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution.name(),
                expectedProfileId
        );

        final var actualExceptions = createLinkUseCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorsMessages, actualExceptions.getErrors());

        Assertions.assertEquals(0, linkRepository.count());

        Mockito.verify(linkGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAnInvalidProfileId_whenCallsCreateLink_shouldReturnNotFound() {
        final var expectedTitle = "a";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS);
        final var expectedLinkExecution = LinkExecutions.EVERY_DAYS;
        final var expectedProfileId = "123";

        final var expectedErrorMessage = "Profile with ID 123 was not found";

        Assertions.assertEquals(0, linkRepository.count());

        final var aCommand = new CreateLinkCommand(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution.name(),
                expectedProfileId
        );

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> createLinkUseCase.execute(aCommand).getLeft()
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Assertions.assertEquals(0, linkRepository.count());

        Mockito.verify(linkGateway, Mockito.times(0)).create(Mockito.any());
    }
}
