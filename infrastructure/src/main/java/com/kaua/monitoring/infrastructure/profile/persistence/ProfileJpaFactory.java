package com.kaua.monitoring.infrastructure.profile.persistence;

import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.ProfileID;
import com.kaua.monitoring.infrastructure.utils.EntityConvert;

public final class ProfileJpaFactory extends EntityConvert<ProfileJpaEntity, Profile> {

    private ProfileJpaFactory() {}

    @Override
    public ProfileJpaEntity toEntity(Profile aDomain) {
        return new ProfileJpaEntity(
                aDomain.getId().getValue(),
                aDomain.getUserId(),
                aDomain.getAvatarUrl(),
                aDomain.getType()
        );
    }

    @Override
    public Profile toDomain(ProfileJpaEntity aEntity) {
        return new Profile(
                ProfileID.from(aEntity.getId()),
                aEntity.getUserId(),
                aEntity.getAvatarUrl(),
                aEntity.getType()
        );
    }
}
