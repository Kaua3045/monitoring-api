package com.kaua.monitoring.infrastructure.application.link.retrieve.get;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdCommand;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdUseCase;
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

@IntegrationTest
public class GetLinkByIdUseCaseIT {

    @Autowired
    private GetLinkByIdUseCase getLinkByIdUseCase;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @SpyBean
    private LinkGateway linkGateway;

    @Test
    public void givenAnValidCommand_whenCallsGetLinkById_shouldReturnLink() {
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
                LinkExecutions.NO_REPEAT,
                aProfile
        );

        linkRepository.save(LinkJpaFactory.toEntity(aLink));

        final var expectedId = aLink.getId().getValue();

        Assertions.assertEquals(1, linkRepository.count());

        final var aCommand = new GetLinkByIdCommand(expectedId);

        final var actualOutput = getLinkByIdUseCase.execute(aCommand);

        Assertions.assertEquals(expectedId, actualOutput.id());
        Assertions.assertEquals(aLink.getTitle(), actualOutput.title());
        Assertions.assertEquals(aLink.getUrl(), actualOutput.url());
        Assertions.assertNotNull(actualOutput.executeDateFormatted());
        Assertions.assertEquals(aLink.getLinkExecution().getName(), actualOutput.linkExecution());
        Assertions.assertEquals(aLink.getProfile().getId().getValue(), actualOutput.profileId());

        Mockito.verify(linkGateway, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    public void givenAnInvalidLinkId_whenCallsGetLinkById_shouldReturnNotFound() {
        final var expectedLinkId = "123";

        final var expectedErrorMessage = "Link with ID 123 was not found";

        Assertions.assertEquals(0, linkRepository.count());

        final var aCommand = new GetLinkByIdCommand(expectedLinkId);

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> getLinkByIdUseCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Assertions.assertEquals(0, linkRepository.count());

        Mockito.verify(linkGateway, Mockito.times(1)).findById(Mockito.any());
    }
}
