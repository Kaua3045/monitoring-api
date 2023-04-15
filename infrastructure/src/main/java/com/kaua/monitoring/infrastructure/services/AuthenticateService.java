package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.security.CreateAuthenticateCommand;
import com.kaua.monitoring.application.usecases.security.CreateAuthenticateUseCase;
import com.kaua.monitoring.application.usecases.security.outputs.AuthenticateTokenOutput;
import com.kaua.monitoring.infrastructure.profile.inputs.AuthenticateProfileBody;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateService {

    private final CreateAuthenticateUseCase createAuthenticateUseCase;

    public AuthenticateService(CreateAuthenticateUseCase createAuthenticateUseCase) {
        this.createAuthenticateUseCase = createAuthenticateUseCase;
    }

    public AuthenticateTokenOutput authenticate(AuthenticateProfileBody body) {
        final var aCommand = new CreateAuthenticateCommand(body.email(), body.password());

        final var aToken = this.createAuthenticateUseCase.execute(aCommand);

        if (aToken.isLeft()) {
            throw aToken.getLeft();
        }

        return aToken.getRight();
    }
}
