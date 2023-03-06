package com.kaua.monitoring.application.usecases.link.delete;

import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.profile.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteLinkUseCaseTest {

    @InjectMocks
    private DefaultDeleteLinkUseCase useCase;

    @Mock
    private LinkGateway linkGateway;

    @Test
    public void givenAnValidId_whenCallsDelete_shouldBeOk() {
        final var aLink = Link.newLink(
                "teste",
                "https://localhost.com",
                LocalDateTime.ofInstant(
                        Instant.now().plus(5, ChronoUnit.DAYS),
                        ZoneId.of("America/Sao_Paulo")),
                true,
                Profile.newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                )
        );

        final var expectedId = aLink.getId().getValue();

        doNothing()
                .when(linkGateway).deleteById(expectedId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(new DeleteLinkCommand(expectedId)));

        Mockito.verify(linkGateway, times(1)).deleteById(any());
    }

    @Test
    public void givenAnInvalidId_whenCallsDelete_shouldBeOk() {
        final var expectedId = "123";

        doNothing()
                .when(linkGateway).deleteById(expectedId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(new DeleteLinkCommand(expectedId)));

        Mockito.verify(linkGateway, times(1)).deleteById(any());
    }
}
