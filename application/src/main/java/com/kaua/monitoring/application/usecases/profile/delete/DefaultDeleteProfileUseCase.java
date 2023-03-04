package com.kaua.monitoring.application.usecases.profile.delete;

import com.kaua.monitoring.application.gateways.ProfileGateway;

public class DefaultDeleteProfileUseCase extends DeleteProfileUseCase {

    private final ProfileGateway profileGateway;

    public DefaultDeleteProfileUseCase(ProfileGateway profileGateway) {
        this.profileGateway = profileGateway;
    }

    @Override
    public void execute(DeleteProfileCommand aCommand) {
        this.profileGateway.deleteById(aCommand.profileId());
    }
}
