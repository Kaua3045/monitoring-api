package com.kaua.monitoring.application.usecases.client.create;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.EmailAlreadyExistsException;
import com.kaua.monitoring.application.gateways.ClientGateway;
import com.kaua.monitoring.domain.client.Client;
import com.kaua.monitoring.domain.client.ClientType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateClientUseCaseTest {

    @InjectMocks
    private DefaultCreateClientUseCase usecase;

    @Mock
    private ClientGateway clientGateway;

    @Test
    public void givenAnValidCommand_whenCallsCreateClientUseCase_shouldReturnClientId() {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "12345678";

        final var expectedEmailVerified = false;
        final var expectedClientType = ClientType.FREE;

        when(clientGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = new CreateClientCommand(
                expectedName,
                expectedEmail,
                expectedPassword
        );

        final var aClient = usecase.execute(aCommand).getRight();

        Assertions.assertNotNull(aClient.id());

        Mockito.verify(clientGateway, times(1)).create(argThat(client ->
                Objects.equals(aClient.id(), client.getId().getValue())
                && Objects.equals(expectedName, client.getName())
                && Objects.equals(expectedEmail, client.getEmail())
                && Objects.equals(expectedPassword, client.getPassword())
                && Objects.equals(expectedClientType, client.getType())
                && Objects.equals(expectedEmailVerified, client.isEmailVerified())
                && Objects.nonNull(client.getCreatedAt())
                && Objects.nonNull(client.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateClientUseCase_shouldReturnDomainException() {
        final String expectedName = null;
        final var expectedEmail = " ";
        final var expectedPassword = "1234567";

        final var expectedErrorOne = "'name' should not be null or empty";
        final var expectedErrorTwo = "'email' should not be null or empty";
        final var expectedErrorThree = "'password' must contain 8 characters at least";

        final var aCommand = new CreateClientCommand(
                expectedName,
                expectedEmail,
                expectedPassword
        );

        final var actualException = usecase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorTwo, actualException.getErrors().get(1).message());
        Assertions.assertEquals(expectedErrorThree, actualException.getErrors().get(2).message());

        Mockito.verify(clientGateway, times(0)).create(any());
    }

    @Test
    public void givenAnValidCommand_whenCallsCreateClientUseCase_shouldReturnEmailAlreadyExists() {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "12345678";

        final var expectedErrorMessage = "Email already exists";

        when(clientGateway.findByEmail(eq(expectedEmail)))
                .thenReturn(Optional.of(Client.newClient(
                        expectedName,
                        expectedEmail,
                        expectedPassword
                )));

        final var aCommand = new CreateClientCommand(
                expectedName,
                expectedEmail,
                expectedPassword
        );

        final var actualException = usecase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(clientGateway, times(1)).findByEmail(expectedEmail);
        Mockito.verify(clientGateway, times(0)).create(any());
    }
}
