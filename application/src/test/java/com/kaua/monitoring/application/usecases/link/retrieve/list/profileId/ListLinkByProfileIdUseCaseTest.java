package com.kaua.monitoring.application.usecases.link.retrieve.list.profileId;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.profile.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListLinkByProfileIdUseCaseTest {

    @InjectMocks
    private DefaultListLinkByProfileIdUseCase useCase;

    @Mock
    private LinkGateway linkGateway;

    @Mock
    private ProfileGateway profileGateway;

    @Test
    public void givenAnValidProfileIdAndPrePersistedLinks_whenCallsFindAllByProfileId_shouldReturnLinks() {
        final var expectedProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        final var expectedLinks = List.of(
                Link.newLink(
                        "teste 1",
                        "https://localhost.com",
                        Instant.now().plus(5, ChronoUnit.DAYS),
                        true,
                        expectedProfile
                ),
                Link.newLink(
                        "teste 2",
                        "https://localhost.com",
                        Instant.now().plus(5, ChronoUnit.DAYS),
                        false,
                        expectedProfile
                )
        );

        when(profileGateway.findById(any()))
                .thenReturn(Optional.of(expectedProfile));

        when(linkGateway.findAllByProfileId(any()))
                .thenReturn(expectedLinks);

        final var aCommand = new ListLinkByProfileIdCommand(expectedProfile.getId().getValue());

        final var actualLinks = useCase.execute(aCommand);

        Assertions.assertEquals(expectedLinks.get(0).getTitle(), actualLinks.get(0).title());
        Assertions.assertEquals(expectedLinks.get(1).getTitle(), actualLinks.get(1).title());
    }

    @Test
    public void givenAnValidProfileId_whenCallsFindAllByProfileId_shouldReturnLinks() {
        final var expectedProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        final var expectedLinks = List.<Link>of();

        when(profileGateway.findById(any()))
                .thenReturn(Optional.of(expectedProfile));

        when(linkGateway.findAllByProfileId(any()))
                .thenReturn(expectedLinks);

        final var aCommand = new ListLinkByProfileIdCommand(expectedProfile.getId().getValue());

        final var actualLinks = useCase.execute(aCommand);

        Assertions.assertEquals(expectedLinks.size(), actualLinks.size());
    }

    @Test
    public void givenAnInvalidProfileId_whenCallsFindAllByProfileId_shouldThrowNotFoundException() {
        final var expectedProfile = "123";
        final var expectedErrorMessage = "Profile with ID 123 was not found";

        when(profileGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var aCommand = new ListLinkByProfileIdCommand(expectedProfile);

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
