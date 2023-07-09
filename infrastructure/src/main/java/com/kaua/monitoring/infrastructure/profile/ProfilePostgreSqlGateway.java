package com.kaua.monitoring.infrastructure.profile;

import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaFactory;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProfilePostgreSqlGateway implements ProfileGateway {

    private final ProfileRepository profileRepository;

    public ProfilePostgreSqlGateway(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile create(Profile aProfile) {
        final var aEntity = this.profileRepository.save(ProfileJpaFactory.toEntity(aProfile));
        return ProfileJpaFactory.toDomain(aEntity);
    }

    @Override
    public Optional<Profile> findById(String profileId) {
        return this.profileRepository.findById(profileId)
                .map(ProfileJpaFactory::toDomain);
    }

    @Override
    public Optional<Profile> findByEmail(String email) {
        return this.profileRepository.findByEmail(email)
                .map(ProfileJpaFactory::toDomain);
    }

    @Override
    public Profile update(Profile aProfile) {
        final var aEntity = this.profileRepository.save(ProfileJpaFactory.toEntity(aProfile));
        return ProfileJpaFactory.toDomain(aEntity);
    }

    @Override
    public void deleteById(String profileId) {
        if (this.profileRepository.existsById(profileId)) {
            this.profileRepository.deleteById(profileId);
        }
    }
}
