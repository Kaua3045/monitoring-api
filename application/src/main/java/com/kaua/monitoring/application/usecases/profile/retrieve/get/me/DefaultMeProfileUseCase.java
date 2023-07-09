package com.kaua.monitoring.application.usecases.profile.retrieve.get.me;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.JwtGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.outputs.ProfileOutput;
import com.kaua.monitoring.domain.profile.Profile;

import java.util.Objects;

public class DefaultMeProfileUseCase extends MeProfileUseCase {

    private final ProfileGateway profileGateway;
    private final JwtGateway jwtGateway;

    public DefaultMeProfileUseCase(
            final ProfileGateway profileGateway,
            final JwtGateway jwtGateway
    ) {
        this.profileGateway = Objects.requireNonNull(profileGateway);
        this.jwtGateway = Objects.requireNonNull(jwtGateway);
    }

    @Override
    public ProfileOutput execute(MeProfileCommand aCommand) {
        final var aTokenSubject = this.jwtGateway.extractTokenSubject(aCommand.token());

        return ProfileOutput.from(this.profileGateway.findById(aTokenSubject)
                .orElseThrow(() -> new NotFoundException(Profile.class, aTokenSubject)));
    }
}
