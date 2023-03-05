package com.kaua.monitoring.infrastructure.profile.persistence;

import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.ProfileID;

public final class ProfileJpaFactory {

    private ProfileJpaFactory() {}

    public static ProfileJpaEntity toEntity(Profile aDomain) {
        return new ProfileJpaEntity(
                aDomain.getId().getValue(),
                aDomain.getUserId(),
                aDomain.getUsername(),
                aDomain.getEmail(),
                aDomain.getAvatarUrl(),
                aDomain.getType()
        );
    }

    public static Profile toDomain(ProfileJpaEntity aEntity) {
        return new Profile(
                ProfileID.from(aEntity.getId()),
                aEntity.getUserId(),
                aEntity.getUsername(),
                aEntity.getEmail(),
                aEntity.getAvatarUrl(),
                aEntity.getType()
        );
    }
}
