package com.kaua.monitoring.application.usecases.link.update;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.usecases.UseCase;
import com.kaua.monitoring.application.usecases.link.outputs.UpdateLinkOutput;

public abstract class UpdateLinkUseCase extends UseCase<
        Either<DomainException, UpdateLinkOutput>, UpdateLinkCommand> {
}
