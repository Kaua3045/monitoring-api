package com.kaua.monitoring.application.usecases.client.create;

import com.kaua.monitoring.application.gateways.ClientGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateClientUseCaseTest {

    @InjectMocks
    private DefaultCreateClientUseCase usecase;

    @Mock
    private ClientGateway clientGateway;

    @Test
    public void givenAnValidCommand_whenCallsCreateClientUseCase_shouldReturnClientCreated() {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "12345678";

        final var aCommand = new CreateClientCommand(
                expectedName,
                expectedEmail,
                expectedPassword
        );

        when(clientGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var aClient = usecase.execute(aCommand).getRight();

        Assertions.assertNotNull(aClient.id());
    }
}
