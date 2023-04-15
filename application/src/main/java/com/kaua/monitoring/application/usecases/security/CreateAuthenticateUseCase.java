package com.kaua.monitoring.application.usecases.security;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.usecases.UseCase;
import com.kaua.monitoring.application.usecases.security.outputs.AuthenticateTokenOutput;

public abstract class CreateAuthenticateUseCase extends UseCase<
        Either<NotFoundException, AuthenticateTokenOutput>, CreateAuthenticateCommand> {
}
