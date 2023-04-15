package com.kaua.monitoring.infrastructure.profile.persistence;

import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.ProfileID;

public final class ProfileJpaFactory {

    private ProfileJpaFactory() {}

    public static ProfileJpaEntity toEntity(Profile aDomain) {
        return new ProfileJpaEntity(
                aDomain.getId().getValue(),
                aDomain.getUsername(),
                aDomain.getEmail(),
                aDomain.getPassword(),
                aDomain.getAvatarUrl(),
                aDomain.getType()
        );
    }

    public static Profile toDomain(ProfileJpaEntity aEntity) {
        return new Profile(
                ProfileID.from(aEntity.getId()),
                aEntity.getUsername(),
                aEntity.getEmail(),
                aEntity.getPassword(),
                aEntity.getAvatarUrl(),
                aEntity.getType()
        );
    }
}
