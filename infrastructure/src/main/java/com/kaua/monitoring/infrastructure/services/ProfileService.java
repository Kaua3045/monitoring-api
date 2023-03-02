package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.profile.create.CreateProfileCommand;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.outputs.CreateProfileOutput;
import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import org.springframework.stereotype.Component;

@Component
public class ProfileService {

    private final CreateProfileUseCase createProfileUseCase;

    public ProfileService(CreateProfileUseCase createProfileUseCase) {
        this.createProfileUseCase = createProfileUseCase;
    }

    public CreateProfileOutput createProfile(CreateProfileBody body) {
        final var aCommand = new CreateProfileCommand(body.userId(), body.avatarUrl());
        final var aResult = this.createProfileUseCase.execute(aCommand);

        if (aResult.isLeft()) {
            throw aResult.getLeft();
        }

        return aResult.getRight();
    }
}
