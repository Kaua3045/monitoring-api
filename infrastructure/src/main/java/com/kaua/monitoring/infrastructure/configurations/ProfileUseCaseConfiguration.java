package com.kaua.monitoring.infrastructure.configurations;

import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.create.DefaultCreateProfileUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileUseCaseConfiguration {

    private final ProfileGateway profileGateway;

    public ProfileUseCaseConfiguration(ProfileGateway profileGateway) {
        this.profileGateway = profileGateway;
    }

    @Bean
    public CreateProfileUseCase createProfileUseCase() {
        return new DefaultCreateProfileUseCase(profileGateway);
    }
}
