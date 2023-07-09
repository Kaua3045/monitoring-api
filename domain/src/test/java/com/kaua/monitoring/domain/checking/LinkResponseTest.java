package com.kaua.monitoring.domain.checking;

import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.domain.profile.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class LinkResponseTest {

    @Test
    public void givenAnValidValues_whenCallsNewLinkResponse_shouldReturnLinkResponseCreated() {
        final var expectedResponseMessage = "OK";
        final var expectedStatusCode = 200;
        final var expectedVerifiedDate = Instant.now();
        final var expectedLink = Link.newLink(
                "teste",
                "https://teste.com",
                Instant.now().plus(5, ChronoUnit.DAYS),
                Instant.now().plus(5, ChronoUnit.DAYS),
                LinkExecutions.NO_REPEAT,
                Profile.newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                )
        );

        final var aLinkResponse = LinkResponse.newLinkResponse(
                expectedResponseMessage,
                expectedStatusCode,
                expectedVerifiedDate,
                0,
                expectedLink
        );

        Assertions.assertNotNull(aLinkResponse);
        Assertions.assertNotNull(aLinkResponse.getId());
        Assertions.assertEquals(expectedResponseMessage, aLinkResponse.getResponseMessage());
        Assertions.assertEquals(expectedStatusCode, aLinkResponse.getStatusCode());
        Assertions.assertEquals(expectedLink, aLinkResponse.getLink());
    }
}
