package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.application.usecases.profile.create.CreateProfileCommand;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileUseCase;
import com.kaua.monitoring.infrastructure.api.ProfileAPI;
import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController implements ProfileAPI {

    private final CreateProfileUseCase createProfileUseCase;

    public ProfileController(CreateProfileUseCase createProfileUseCase) {
        this.createProfileUseCase = createProfileUseCase;
    }

    @Override
    public ResponseEntity<?> create(CreateProfileBody body) {
        final var aCommand = new CreateProfileCommand(body.userId(), body.avatarUrl());
        final var aResult = this.createProfileUseCase.execute(aCommand);

        if (aResult.isLeft()) {
            throw aResult.getLeft();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(aResult.getRight());
    }
}
