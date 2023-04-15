package com.kaua.monitoring.infrastructure.configurations.usecases;

import com.kaua.monitoring.application.gateways.AvatarGateway;
import com.kaua.monitoring.application.gateways.EncrypterGateway;
import com.kaua.monitoring.application.gateways.JwtGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.create.DefaultCreateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.delete.DefaultDeleteProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.DefaultGetProfileByUserIdUseCase;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.GetProfileByUserIdUseCase;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.me.DefaultMeProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.me.MeProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.update.DefaultUpdateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.update.UpdateProfileUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileUseCaseConfiguration {

    private final ProfileGateway profileGateway;
    private final AvatarGateway avatarGateway;
    private final JwtGateway jwtGateway;
    private final EncrypterGateway encrypterGateway;

    public ProfileUseCaseConfiguration(
            ProfileGateway profileGateway,
            AvatarGateway avatarGateway,
            JwtGateway jwtGateway,
            EncrypterGateway encrypterGateway
    ) {
        this.profileGateway = profileGateway;
        this.avatarGateway = avatarGateway;
        this.jwtGateway = jwtGateway;
        this.encrypterGateway = encrypterGateway;
    }

    @Bean
    public CreateProfileUseCase createProfileUseCase() {
        return new DefaultCreateProfileUseCase(profileGateway, encrypterGateway, jwtGateway);
    }

    @Bean
    public GetProfileByUserIdUseCase getProfileByUserIdUseCase() {
        return new DefaultGetProfileByUserIdUseCase(profileGateway);
    }

    @Bean
    public MeProfileUseCase meProfileUseCase() {
        return new DefaultMeProfileUseCase(profileGateway, jwtGateway);
    }

    @Bean
    public UpdateProfileUseCase updateProfileUseCase() {
        return new DefaultUpdateProfileUseCase(profileGateway, avatarGateway, encrypterGateway);
    }

    @Bean
    public DeleteProfileUseCase deleteProfileUseCase() {
        return new DefaultDeleteProfileUseCase(profileGateway, avatarGateway);
    }
}
