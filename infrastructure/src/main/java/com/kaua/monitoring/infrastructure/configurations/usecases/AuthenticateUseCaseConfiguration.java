package com.kaua.monitoring.infrastructure.configurations.usecases;

import com.kaua.monitoring.application.gateways.EncrypterGateway;
import com.kaua.monitoring.application.gateways.JwtGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.security.CreateAuthenticateUseCase;
import com.kaua.monitoring.application.usecases.security.DefaultCreateAuthenticateUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticateUseCaseConfiguration {

    private final ProfileGateway profileGateway;
    private final JwtGateway jwtGateway;
    private final EncrypterGateway encrypterGateway;

    public AuthenticateUseCaseConfiguration(
            final ProfileGateway profileGateway,
            final JwtGateway jwtGateway,
            final EncrypterGateway encrypterGateway
    ) {
        this.profileGateway = profileGateway;
        this.jwtGateway = jwtGateway;
        this.encrypterGateway = encrypterGateway;
    }

    @Bean
    public CreateAuthenticateUseCase createAuthenticateUseCase() {
        return new DefaultCreateAuthenticateUseCase(profileGateway, jwtGateway, encrypterGateway);
    }
}
