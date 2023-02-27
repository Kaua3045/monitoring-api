package com.kaua.monitoring.application.usecases.client.create;

import com.kaua.monitoring.application.UseCase;
import com.kaua.monitoring.application.either.Either;
import com.kaua.monitoring.application.exceptions.NoStackTraceException;
import com.kaua.monitoring.application.usecases.client.output.ClientIdOutput;

public abstract class CreateClientUseCase extends UseCase<
        Either<NoStackTraceException, ClientIdOutput>,
        CreateClientCommand> {
}
