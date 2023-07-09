package com.kaua.monitoring.application.usecases.profile.delete;

import com.kaua.monitoring.application.gateways.AvatarGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;

public class DefaultDeleteProfileUseCase extends DeleteProfileUseCase {

    private final ProfileGateway profileGateway;
    private final AvatarGateway avatarGateway;

    public DefaultDeleteProfileUseCase(ProfileGateway profileGateway, AvatarGateway avatarGateway) {
        this.profileGateway = profileGateway;
        this.avatarGateway = avatarGateway;
    }

    @Override
    public void execute(DeleteProfileCommand aCommand) {
        this.avatarGateway.deleteByProfileId(aCommand.profileId());
        this.profileGateway.deleteById(aCommand.profileId());
    }
}
