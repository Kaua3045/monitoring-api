package com.kaua.monitoring.application.usecases.client.create;

import com.kaua.monitoring.application.either.Either;
import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.EmailAlreadyExistsException;
import com.kaua.monitoring.application.gateways.ClientGateway;
import com.kaua.monitoring.application.usecases.client.output.ClientIdOutput;
import com.kaua.monitoring.domain.client.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultCreateClientUseCase extends CreateClientUseCase {

    private final ClientGateway clientGateway;

    @Override
    public Either<DomainException, ClientIdOutput> execute(CreateClientCommand aCommand) {
        if (this.clientGateway.findByEmail(aCommand.email()).isPresent()) {
            return Either.left(DomainException.with(EmailAlreadyExistsException.with()));
        }

        final var aClient = Client.newClient(
                aCommand.name(),
                aCommand.email(),
                aCommand.password()
        );
        final var aClientValidate = aClient.validate();

        if (!aClientValidate.isEmpty()) {
            return Either.left(DomainException.with(aClientValidate));
        }

        this.clientGateway.create(aClient);

        return Either.right(ClientIdOutput.from(aClient.getId().getValue()));
    }
}
