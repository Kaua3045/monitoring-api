package com.kaua.monitoring.application.usecases.link.retrieve.get;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.domain.profile.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetLinkByIdUseCaseTest {

    @InjectMocks
    private DefaultGetLinkByIdUseCase useCase;

    @Mock
    private LinkGateway linkGateway;

    @Test
    public void givenAnValidId_whenCallsFindById_shouldReturnLink() {
        final var expectedTitle = "teste";
        final var expectedUrl = "https://localhost.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS);
        final var expectedRepeat = LinkExecutions.NO_REPEAT;
        final var expectedProfile = Profile
                .newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );

        final var aLink = Link.newLink(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedRepeat,
                expectedProfile
        );
        final var expectedId = aLink.getId().getValue();

        when(linkGateway.findById(any()))
                .thenReturn(Optional.of(aLink));

        final var aCommand = new GetLinkByIdCommand(expectedId);

        final var actualLink = useCase.execute(aCommand);

        Assertions.assertNotNull(actualLink);
        Assertions.assertEquals(expectedId, actualLink.id());
        Assertions.assertEquals(expectedTitle, actualLink.title());
        Assertions.assertEquals(expectedUrl, actualLink.url());
        Assertions.assertNotNull(actualLink.executeDateFormatted());
        Assertions.assertEquals(expectedRepeat.name(), actualLink.linkExecutions());
        Assertions.assertEquals(expectedProfile.getId().getValue(), actualLink.profileId());
    }

    @Test
    public void givenAnInvalidId_whenCallsFindById_shouldThrowNotFoundException() {
        final var expectedId = "123";
        final var expectedErrorMessage = "Link with ID 123 was not found";

        when(linkGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var aCommand = new GetLinkByIdCommand(expectedId);

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
