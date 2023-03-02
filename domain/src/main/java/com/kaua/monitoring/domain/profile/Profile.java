package com.kaua.monitoring.domain.profile;

import com.kaua.monitoring.domain.Aggregate;
import lombok.Getter;

@Getter
public class Profile extends Aggregate<ProfileID> {

    private String userId;
    private String avatarUrl;
    private VersionAccountType type;

    public Profile(
            final String aId,
            final String aUserId,
            final String aAvatarUrl,
            final VersionAccountType aType
    ) {
        super(aId);
        this.userId = aUserId;
        this.avatarUrl = aAvatarUrl;
        this.type = aType;
    }

    public static Profile newProfile(final String aUserId, final String avatarUrl) {
        return new Profile(
                ProfileID.unique().getValue(),
                aUserId,
                avatarUrl,
                VersionAccountType.FREE
        );
    }

    public Profile update(final String avatarUrl, final VersionAccountType type) {
        this.avatarUrl = avatarUrl;
        this.type = type;
        return this;
    }
}
