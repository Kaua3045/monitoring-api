package com.kaua.monitoring.application.usecases.profile.update;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.usecases.UseCase;
import com.kaua.monitoring.application.usecases.profile.outputs.ProfileOutput;

public abstract class UpdateProfileUseCase extends UseCase<
        Either<DomainException, ProfileOutput>, UpdateProfileCommand> {
}
