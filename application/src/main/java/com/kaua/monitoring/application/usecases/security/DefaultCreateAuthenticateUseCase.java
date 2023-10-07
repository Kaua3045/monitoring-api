package com.kaua.monitoring.application.usecases.security;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.either.Either;
import com.kaua.monitoring.application.gateways.EncrypterGateway;
import com.kaua.monitoring.application.gateways.JwtGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.security.outputs.AuthenticateTokenOutput;
import com.kaua.monitoring.domain.profile.Profile;

public class DefaultCreateAuthenticateUseCase extends CreateAuthenticateUseCase {

    private final ProfileGateway profileGateway;
    private final JwtGateway jwtGateway;
    private final EncrypterGateway encrypterGateway;

    public DefaultCreateAuthenticateUseCase(
            ProfileGateway profileGateway,
            JwtGateway jwtGateway,
            EncrypterGateway encrypterGateway
    ) {
        this.profileGateway = profileGateway;
        this.jwtGateway = jwtGateway;
        this.encrypterGateway = encrypterGateway;
    }

    @Override
    public Either<NotFoundException, AuthenticateTokenOutput> execute(CreateAuthenticateCommand aCommand) {
        final var aProfile = this.profileGateway.findByEmail(aCommand.email());

        if (aProfile.isEmpty()) {
            return Either.left(new NotFoundException(Profile.class, aCommand.email()));
        }

        if (!this.encrypterGateway.isMatch(aCommand.password(), aProfile.get().getPassword())) {
            return Either.left(new NotFoundException(Profile.class, aCommand.email()));
        }

        final var aToken = this.jwtGateway.generateToken(aProfile.get().getId().getValue());

        return Either.right(new AuthenticateTokenOutput(aToken));
    }
}
