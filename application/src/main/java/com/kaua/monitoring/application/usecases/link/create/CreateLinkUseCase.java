package com.kaua.monitoring.application.usecases.link.create;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.usecases.UseCase;
import com.kaua.monitoring.application.usecases.link.outputs.CreateLinkOutput;

public abstract class CreateLinkUseCase extends UseCase<
        Either<DomainException, CreateLinkOutput>, CreateLinkCommand> {
}
