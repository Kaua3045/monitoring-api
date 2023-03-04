package com.kaua.monitoring.application.usecases.profile.update;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.outputs.ProfileOutput;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.VersionAccountType;

public class DefaultUpdateProfileUseCase extends UpdateProfileUseCase {

    private final ProfileGateway profileGateway;

    public DefaultUpdateProfileUseCase(final ProfileGateway profileGateway) {
        this.profileGateway = profileGateway;
    }

    @Override
    public Either<DomainException, ProfileOutput> execute(UpdateProfileCommand aCommand) {
        final var aProfileExists = this.profileGateway.findById(aCommand.profileId())
                .orElseThrow(() -> new NotFoundException(Profile.class, aCommand.profileId()));

        final var aProfileUpdated = aProfileExists.update(
                aCommand.username(),
                aCommand.avatarUrl(),
                VersionAccountType.valueOf(aCommand.type())
        );

        final var aProfileUpdatedValidate = aProfileUpdated.validate();

        if (!aProfileUpdatedValidate.isEmpty()) {
            return Either.left(DomainException.with(aProfileUpdatedValidate));
        }

        return Either.right(ProfileOutput.from(this.profileGateway.update(aProfileUpdated)));
    }
}
