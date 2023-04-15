package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.profile.create.CreateProfileCommand;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileCommand;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.outputs.CreateProfileOutput;
import com.kaua.monitoring.application.usecases.profile.outputs.ProfileOutput;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.GetProfileByUserIdUseCase;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.GetProfileCommand;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.me.MeProfileCommand;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.me.MeProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.update.UpdateProfileCommand;
import com.kaua.monitoring.application.usecases.profile.update.UpdateProfileUseCase;
import com.kaua.monitoring.domain.profile.Resource;
import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import org.springframework.stereotype.Component;

@Component
public class ProfileService {

    private final CreateProfileUseCase createProfileUseCase;
    private final GetProfileByUserIdUseCase getProfileByUserIdUseCase;
    private final MeProfileUseCase meProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final DeleteProfileUseCase deleteProfileUseCase;


    public ProfileService(
            final CreateProfileUseCase createProfileUseCase,
            final GetProfileByUserIdUseCase getProfileByUserIdUseCase,
            final MeProfileUseCase meProfileUseCase,
            final UpdateProfileUseCase updateProfileUseCase,
            final DeleteProfileUseCase deleteProfileUseCase
    ) {
        this.createProfileUseCase = createProfileUseCase;
        this.getProfileByUserIdUseCase = getProfileByUserIdUseCase;
        this.meProfileUseCase = meProfileUseCase;
        this.updateProfileUseCase = updateProfileUseCase;
        this.deleteProfileUseCase = deleteProfileUseCase;
    }

    public CreateProfileOutput createProfile(CreateProfileBody body) {
        final var aCommand = new CreateProfileCommand(
                body.username(),
                body.email(),
                body.password(),
                body.avatarUrl());

        final var aResult = this.createProfileUseCase.execute(aCommand);

        if (aResult.isLeft()) {
            throw aResult.getLeft();
        }

        return aResult.getRight();
    }

    public ProfileOutput getByProfileId(String profileId) {
        final var aCommand = new GetProfileCommand(profileId);

        return this.getProfileByUserIdUseCase.execute(aCommand);
    }

    public ProfileOutput getProfileByToken(String token) {
        final var aCommand = new MeProfileCommand(token);
        return this.meProfileUseCase.execute(aCommand);
    }

    public ProfileOutput updateProfile(
            String profileId,
            String username,
            String password,
            Resource avatarUrl,
            String type
    ) {
        final var aCommand = new UpdateProfileCommand(
                profileId,
                username,
                password,
                avatarUrl,
                type
        );
        final var aResult = this.updateProfileUseCase.execute(aCommand);

        if (aResult.isLeft()) {
            throw aResult.getLeft();
        }

        return aResult.getRight();
    }

    public void deleteProfile(String profileId) {
        final var aCommand = new DeleteProfileCommand(profileId);
        this.deleteProfileUseCase.execute(aCommand);
    }
}
