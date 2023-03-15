package com.kaua.monitoring.infrastructure.checking;

import com.kaua.monitoring.domain.checking.LinkResponse;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.infrastructure.PostgreSqlGatewayTest;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseJpaFactory;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseRepository;
import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaFactory;
import com.kaua.monitoring.infrastructure.link.persistence.LinkRepository;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaFactory;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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


    @Test
    public void givenAnValidUrlIdAndPrePersistedResponses_whenCallsFindAllTop90_shouldReturnLinkResponseList() {
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

        final var actualOutput = linkResponseGateway
                .findAllTop90(expectedLink.getId().getValue());

        Assertions.assertEquals(expectedLinkResponses.size(), actualOutput.size());
        Assertions.assertEquals(expectedLinkResponses.get(0).getId(), actualOutput.get(0).getId().getValue());
    }
}
