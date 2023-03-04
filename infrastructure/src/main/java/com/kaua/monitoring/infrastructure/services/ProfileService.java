package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.profile.create.CreateProfileCommand;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileCommand;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.outputs.CreateProfileOutput;
import com.kaua.monitoring.application.usecases.profile.outputs.ProfileOutput;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.GetProfileByUserIdUseCase;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.GetProfileCommand;
import com.kaua.monitoring.infrastructure.exceptions.UserIdDoesNotMatchException;
import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Component;

@Component
public class ProfileService {

    private final CreateProfileUseCase createProfileUseCase;
    private final GetProfileByUserIdUseCase getProfileByUserIdUseCase;
    private final DeleteProfileUseCase deleteProfileUseCase;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    public ProfileService(
            final CreateProfileUseCase createProfileUseCase,
            final GetProfileByUserIdUseCase getProfileByUserIdUseCase,
            final DeleteProfileUseCase deleteProfileUseCase
    ) {
        this.createProfileUseCase = createProfileUseCase;
        this.getProfileByUserIdUseCase = getProfileByUserIdUseCase;
        this.deleteProfileUseCase = deleteProfileUseCase;
    }

    public CreateProfileOutput createProfile(String token, CreateProfileBody body) {
        final var decoderInstance = JwtDecoders.fromIssuerLocation(issuerUri);
        final var tokenDecoded = decoderInstance.decode(token.substring(7));

        if (!tokenDecoded.getSubject().equalsIgnoreCase(body.userId())) {
            throw new UserIdDoesNotMatchException();
        }

        final var aCommand = new CreateProfileCommand(
                body.userId(),
                body.username(),
                body.email(),
                body.avatarUrl());
        final var aResult = this.createProfileUseCase.execute(aCommand);

        if (aResult.isLeft()) {
            throw aResult.getLeft();
        }

        return aResult.getRight();
    }

    public ProfileOutput getByUserIdProfile(String userId) {
        final var aCommand = new GetProfileCommand(userId);
        final var aResult = this.getProfileByUserIdUseCase.execute(aCommand);

        return aResult;
    }

    public void deleteProfile(String profileId) {
        final var aCommand = new DeleteProfileCommand(profileId);
        this.deleteProfileUseCase.execute(aCommand);
    }
}
