package com.kaua.monitoring.application.usecases.profile.create;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.outputs.CreateProfileOutput;
import com.kaua.monitoring.domain.profile.Profile;

import java.util.Objects;

public class DefaultCreateProfileUseCase extends CreateProfileUseCase {

    private final ProfileGateway profileGateway;

    public DefaultCreateProfileUseCase(ProfileGateway profileGateway) {
        this.profileGateway = Objects.requireNonNull(profileGateway);
    }

    @Override
    public Either<DomainException, CreateProfileOutput> execute(CreateProfileCommand aCommand) {
        final var aProfile = Profile.newProfile(aCommand.userId(), aCommand.avatarUrl());

        final var aProfileValidate = aProfile.validate();

        if (!aProfileValidate.isEmpty()) {
            return Either.left(DomainException.with(aProfileValidate));
        }

        this.profileGateway.create(aProfile);

        return Either.right(new CreateProfileOutput(
                aProfile.getId().getValue(),
                aProfile.getUserId(),
                aProfile.getAvatarUrl(),
                aProfile.getType().name()
        ));
    }
}
