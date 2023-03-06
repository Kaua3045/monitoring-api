package com.kaua.monitoring.infrastructure.link;

import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.infrastructure.PostgreSqlGatewayTest;
import com.kaua.monitoring.infrastructure.link.persistence.LinkRepository;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaFactory;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
        final var expectedRepeat = true;

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
        Assertions.assertEquals(aLink.isRepeat(), actualLink.isRepeat());
        Assertions.assertEquals(aLink.getProfile().getId().getValue(), actualLink.getProfile().getId().getValue());

        final var actualEntity = linkRepository.findById(aLink.getId().getValue()).get();

        Assertions.assertEquals(aLink.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aLink.getTitle(), actualEntity.getTitle());
        Assertions.assertEquals(aLink.getUrl(), actualEntity.getUrl());
        Assertions.assertEquals(aLink.getExecuteDate(), actualEntity.getExecuteDate());
        Assertions.assertEquals(aLink.isRepeat(), actualEntity.isRepeat());
        Assertions.assertEquals(aLink.getProfile().getId().getValue(), actualEntity.getProfile().getId());
    }
}
