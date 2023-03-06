package com.kaua.monitoring.domain.link;

import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.profile.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LinkTest {

    @Test
    public void givenAnValidValues_whenCallsNewLink_shouldReturnLinkCreated() {
        final var expectedTitle = "monitoring";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS);
        final var expectedRepeat = true;
        final var expectedProfile = Profile.
                newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null);

        final var aLink = Link.newLink(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat,
                expectedProfile
        );

        Assertions.assertNotNull(aLink.getId().getValue());
        Assertions.assertEquals(expectedTitle, aLink.getTitle());
        Assertions.assertEquals(expectedUrl, aLink.getUrl());
        Assertions.assertEquals(expectedExecuteDate, aLink.getExecuteDate());
        Assertions.assertEquals(expectedRepeat, aLink.isRepeat());
        Assertions.assertEquals(expectedProfile, aLink.getProfile());
    }

    @Test
    public void givenAnValidValues_whenCallsUpdate_shouldReturnLinkUpdated() {
        final var expectedTitle = "monitoring";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS);
        final var expectedRepeat = true;
        final var expectedProfile = Profile.
                newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null);

        final var aLink = Link.newLink(
                "a",
                "https://localhost.com",
                Instant.now().plus(5, ChronoUnit.DAYS),
                false,
                expectedProfile
        );

        final var aLinkUpdated = aLink.update(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat
        );

        Assertions.assertEquals(0, aLinkUpdated.validate().size());

        Assertions.assertEquals(aLink.getId().getValue(), aLinkUpdated.getId().getValue());
        Assertions.assertEquals(expectedTitle, aLinkUpdated.getTitle());
        Assertions.assertEquals(expectedUrl, aLinkUpdated.getUrl());
        Assertions.assertEquals(expectedExecuteDate, aLinkUpdated.getExecuteDate());
        Assertions.assertEquals(expectedRepeat, aLinkUpdated.isRepeat());
        Assertions.assertEquals(expectedProfile, aLinkUpdated.getProfile());
    }

    @Test
    public void givenAnInvalidValues_whenCallsNewLink_shouldReturnErrorsList() {
        final String expectedTitle = null;
        final var expectedUrl = "a";
        final var expectedExecuteDate = Instant.parse("2007-12-03T10:15:30.00Z");
        final var expectedRepeat = true;
        final Profile expectedProfile = null;

        final var expectedErrorsList = List.of(
                new Error("'title' should not be null or empty"),
                new Error("'url' you must provide a valid url"),
                new Error("'executeDate' cannot be a date that has already passed"),
                new Error("'profile' should not be null")
        );

        final var aLink = Link.newLink(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat,
                expectedProfile
        );

        final var actualExceptions = aLink.validate();

        Assertions.assertEquals(expectedErrorsList, actualExceptions);
    }
}
