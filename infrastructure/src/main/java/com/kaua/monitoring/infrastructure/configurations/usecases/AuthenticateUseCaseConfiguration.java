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

    @Bean
    public CreateAuthenticateUseCase createAuthenticateUseCase(
            ProfileGateway profileGateway,
            JwtGateway jwtGateway,
            EncrypterGateway encrypterGateway
    ) {
        return new DefaultCreateAuthenticateUseCase(profileGateway, jwtGateway, encrypterGateway);
    }
}
