package com.kaua.monitoring.domain.profile;

import com.kaua.monitoring.domain.Aggregate;
import com.kaua.monitoring.domain.exceptions.Error;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Profile extends Aggregate<ProfileID> {

    private String userId;
    private String avatarUrl;
    private VersionAccountType type;

    public Profile(
            final ProfileID profileID,
            final String aUserId,
            final String aAvatarUrl,
            final VersionAccountType aType
    ) {
        super(profileID);
        this.userId = aUserId;
        this.avatarUrl = aAvatarUrl;
        this.type = aType;
    }

    public static Profile newProfile(final String aUserId, final String avatarUrl) {
        return new Profile(
                ProfileID.unique(),
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

    @Override
    public List<Error> validate() {
        final var errors = new ArrayList<Error>();

        if (userId == null || userId.isBlank()) {
            errors.add(new Error("'userId' should not be null or empty"));
        }

        return errors;
    }
}
