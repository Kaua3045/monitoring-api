package com.kaua.monitoring.infrastructure.application.link.delete;

import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.usecases.link.delete.DeleteLinkCommand;
import com.kaua.monitoring.application.usecases.link.delete.DeleteLinkUseCase;
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
public class DeleteLinkUseCaseIT {

    @Autowired
    private DeleteLinkUseCase deleteLinkUseCase;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @SpyBean
    private LinkGateway linkGateway;

    @Test
    public void givenAnValidCommand_whenCallsDeleteLink_shouldBeOk() {
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

        Assertions.assertEquals(1, linkRepository.count());

        final var aCommand = new DeleteLinkCommand(expectedId);

        Assertions.assertDoesNotThrow(() -> deleteLinkUseCase.execute(aCommand));

        Assertions.assertEquals(0, linkRepository.count());

        Mockito.verify(linkGateway, Mockito.times(1)).deleteById(Mockito.any());
    }

    @Test
    public void givenAnInvalidLinkId_whenCallsDeleteLink_shouldBeOk() {
        final var expectedLinkId = "123";

        Assertions.assertEquals(0, linkRepository.count());

        final var aCommand = new DeleteLinkCommand(expectedLinkId);

        Assertions.assertDoesNotThrow(() -> deleteLinkUseCase.execute(aCommand));

        Assertions.assertEquals(0, linkRepository.count());

        Mockito.verify(linkGateway, Mockito.times(1)).deleteById(Mockito.any());
    }
}
