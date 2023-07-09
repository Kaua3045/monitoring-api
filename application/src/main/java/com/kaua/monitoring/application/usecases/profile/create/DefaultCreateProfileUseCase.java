package com.kaua.monitoring.application.usecases.profile.create;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.EmailAlreadyExistsException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.gateways.EncrypterGateway;
import com.kaua.monitoring.application.gateways.JwtGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.outputs.CreateProfileOutput;
import com.kaua.monitoring.domain.profile.Profile;

import java.util.Objects;

public class DefaultCreateProfileUseCase extends CreateProfileUseCase {

    private final ProfileGateway profileGateway;
    private final EncrypterGateway encrypterGateway;
    private final JwtGateway jwtGateway;

    public DefaultCreateProfileUseCase(
            ProfileGateway profileGateway,
            EncrypterGateway encrypterGateway,
            JwtGateway jwtGateway
    ) {
        this.profileGateway = Objects.requireNonNull(profileGateway);
        this.encrypterGateway = encrypterGateway;
        this.jwtGateway = jwtGateway;
    }

    @Override
    public Either<DomainException, CreateProfileOutput> execute(CreateProfileCommand aCommand) {
        final var aProfileExists = this.profileGateway.findByEmail(aCommand.email());

        if (aProfileExists.isPresent()) {
            return Either.left(DomainException.with(EmailAlreadyExistsException.with()));
        }

        final var aProfile = Profile.newProfile(
                aCommand.username(),
                aCommand.email(),
                aCommand.password(),
                aCommand.avatarUrl());

        final var aProfileValidate = aProfile.validate();

        if (!aProfileValidate.isEmpty()) {
            return Either.left(DomainException.with(aProfileValidate));
        }

        final var aProfileAndEncryptedPassword = Profile.with(
                aProfile.getId(),
                aProfile.getUsername(),
                aProfile.getEmail(),
                this.encrypterGateway.encrypt(aCommand.password()),
                aProfile.getAvatarUrl(),
                aProfile.getType()
        );

        this.profileGateway.create(aProfileAndEncryptedPassword);
        final var aAccessToken = this.jwtGateway.generateToken(aProfileAndEncryptedPassword.getId().getValue());

        return Either.right(new CreateProfileOutput(
                aProfile.getId().getValue(),
                aProfile.getUsername(),
                aProfile.getEmail(),
                aProfile.getAvatarUrl(),
                aProfile.getType().name(),
                aAccessToken
        ));
    }
}
