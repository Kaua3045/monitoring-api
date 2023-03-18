package com.kaua.monitoring.infrastructure.configurations.usecases;

import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.create.DefaultCreateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.delete.DefaultDeleteProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.DefaultGetProfileByUserIdUseCase;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.GetProfileByUserIdUseCase;
import com.kaua.monitoring.application.usecases.profile.update.DefaultUpdateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.update.UpdateProfileUseCase;
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

    @Bean
    public GetProfileByUserIdUseCase getProfileByUserIdUseCase() {
        return new DefaultGetProfileByUserIdUseCase(profileGateway);
    }

    @Bean
    public UpdateProfileUseCase updateProfileUseCase() {
        return new DefaultUpdateProfileUseCase(profileGateway);
    }

    @Bean
    public DeleteProfileUseCase deleteProfileUseCase() {
        return new DefaultDeleteProfileUseCase(profileGateway);
    }
}