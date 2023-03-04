package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.profile.create.CreateProfileCommand;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.outputs.CreateProfileOutput;
import com.kaua.monitoring.infrastructure.exceptions.UserIdDoesNotMatchException;
import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Component;

@Component
public class ProfileService {

    private final CreateProfileUseCase createProfileUseCase;

    public ProfileService(CreateProfileUseCase createProfileUseCase) {
        this.createProfileUseCase = createProfileUseCase;
    }

    public CreateProfileOutput createProfile(String token, CreateProfileBody body) {
        final var decoderInstance = JwtDecoders.fromIssuerLocation("http://localhost:8081/auth/realms/teste");
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
}
