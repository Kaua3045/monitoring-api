package com.kaua.monitoring.application.usecases.profile.retrieve.get;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.outputs.ProfileOutput;
import com.kaua.monitoring.domain.profile.Profile;

public class DefaultGetProfileByUserIdUseCase extends GetProfileByUserIdUseCase {

    private final ProfileGateway profileGateway;

    public DefaultGetProfileByUserIdUseCase(ProfileGateway profileGateway) {
        this.profileGateway = profileGateway;
    }

    @Override
    public ProfileOutput execute(GetProfileCommand aCommand) {
        return this.profileGateway.findById(aCommand.profileId())
                .map(ProfileOutput::from)
                .orElseThrow(() -> new NotFoundException(Profile.class, aCommand.profileId()));
    }
}
