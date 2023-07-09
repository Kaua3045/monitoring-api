package com.kaua.monitoring.domain.profile;

import com.kaua.monitoring.domain.Aggregate;
import com.kaua.monitoring.domain.exceptions.Error;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Profile extends Aggregate<ProfileID> {

    private String username;
    private String email;
    private String password;
    private String avatarUrl;
    private VersionAccountType type;

    private final int PASSWORD_MINIMUM_LENGTH = 8;

    public Profile(
            final ProfileID profileID,
            final String aUsername,
            final String aEmail,
            final String aPassword,
            final String aAvatarUrl,
            final VersionAccountType aType
    ) {
        super(profileID);
        this.username = aUsername;
        this.email = aEmail;
        this.password = aPassword;
        this.avatarUrl = aAvatarUrl;
        this.type = aType;
    }

    public static Profile newProfile(
            final String aUsername,
            final String aEmail,
            final String aPassword,
            final String avatarUrl) {
        return new Profile(
                ProfileID.unique(),
                aUsername,
                aEmail,
                aPassword,
                avatarUrl,
                VersionAccountType.FREE
        );
    }

    public static Profile with(
            final ProfileID profileID,
            final String aUsername,
            final String aEmail,
            final String aPassword,
            final String aAvatarUrl,
            final VersionAccountType aType
    ) {
        return new Profile(
                profileID,
                aUsername,
                aEmail,
                aPassword,
                aAvatarUrl,
                aType
        );
    }

    public Profile update(
            final String username,
            final String password,
            final String avatarUrl,
            final VersionAccountType type
    ) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.password = password;
        this.type = type;
        return this;
    }

    @Override
    public List<Error> validate() {
        final var errors = new ArrayList<Error>();

        if (username == null || username.isBlank()) {
            errors.add(new Error("'username' should not be null or empty"));
        }

        if (email == null || email.isBlank()) {
            errors.add(new Error("'email' should not be null or empty"));
        }

        if (password == null || password.isBlank()) {
            errors.add(new Error("'password' should not be null or empty"));
        }

        if (!(password == null) && password.length() < PASSWORD_MINIMUM_LENGTH) {
            errors.add(new Error("'password' must be at least 8 characters"));
        }

        return errors;
    }
}
