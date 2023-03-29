package com.kaua.monitoring.application.usecases.profile.update;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.gateways.AvatarGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.outputs.ProfileOutput;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.VersionAccountType;

public class DefaultUpdateProfileUseCase extends UpdateProfileUseCase {

    private final ProfileGateway profileGateway;
    private final AvatarGateway avatarGateway;

    public DefaultUpdateProfileUseCase(
            final ProfileGateway profileGateway,
            final AvatarGateway avatarGateway
    ) {
        this.profileGateway = profileGateway;
        this.avatarGateway = avatarGateway;
    }

    @Override
    public Either<DomainException, ProfileOutput> execute(UpdateProfileCommand aCommand) {
        final var aProfileExists = this.profileGateway.findById(aCommand.profileId())
                .orElseThrow(() -> new NotFoundException(Profile.class, aCommand.profileId()));

        final var aUsername = aCommand.username() == null || aCommand.username().isBlank()
                ? aProfileExists.getUsername()
                : aCommand.username();

        final var aType = aCommand.type() == null
                ? aProfileExists.getType()
                : VersionAccountType.valueOf(aCommand.type());

        final var avatarUrlStored = aCommand.avatarUrl() == null
                ? aProfileExists.getAvatarUrl()
                : this.avatarGateway.create(
                aProfileExists.getId().getValue(),
                aCommand.avatarUrl()
        );

        final var aProfileUpdated = aProfileExists.update(
                aUsername,
                avatarUrlStored,
                aType
        );

        final var aProfileUpdatedValidate = aProfileUpdated.validate();

        if (!aProfileUpdatedValidate.isEmpty()) {
            return Either.left(DomainException.with(aProfileUpdatedValidate));
        }

        return Either.right(ProfileOutput.from(this.profileGateway.update(aProfileUpdated)));
    }
}
