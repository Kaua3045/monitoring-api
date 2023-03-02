package com.kaua.monitoring.application.usecases.profile.create;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.usecases.UseCase;
import com.kaua.monitoring.application.usecases.profile.outputs.CreateProfileOutput;

public abstract class CreateProfileUseCase extends UseCase<
        Either<DomainException, CreateProfileOutput>, CreateProfileCommand> {
}
