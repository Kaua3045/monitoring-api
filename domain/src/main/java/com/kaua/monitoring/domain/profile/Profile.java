package com.kaua.monitoring.domain.profile;

import com.kaua.monitoring.domain.Aggregate;
import com.kaua.monitoring.domain.exceptions.Error;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Profile extends Aggregate<ProfileID> {

    private String userId;
    private String username;
    private String email;
    private String avatarUrl;
    private VersionAccountType type;

    public Profile(
            final ProfileID profileID,
            final String aUserId,
            final String aUsername,
            final String aEmail,
            final String aAvatarUrl,
            final VersionAccountType aType
    ) {
        super(profileID);
        this.userId = aUserId;
        this.username = aUsername;
        this.email = aEmail;
        this.avatarUrl = aAvatarUrl;
        this.type = aType;
    }

    public static Profile newProfile(
            final String aUserId,
            final String aUsername,
            final String aEmail,
            final String avatarUrl) {
        return new Profile(
                ProfileID.unique(),
                aUserId,
                aUsername,
                aEmail,
                avatarUrl,
                VersionAccountType.FREE
        );
    }

    public static Profile with(
            final ProfileID profileID,
            final String aUserId,
            final String aUsername,
            final String aEmail,
            final String aAvatarUrl,
            final VersionAccountType aType
    ) {
        return new Profile(
                profileID,
                aUserId,
                aUsername,
                aEmail,
                aAvatarUrl,
                aType
        );
    }

    public Profile update(final String username, final String avatarUrl, final VersionAccountType type) {
        this.username = username;
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

        if (username == null || username.isBlank()) {
            errors.add(new Error("'username' should not be null or empty"));
        }

        if (email == null || email.isBlank()) {
            errors.add(new Error("'email' should not be null or empty"));
        }

        return errors;
    }
}
