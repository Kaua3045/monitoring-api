package com.kaua.monitoring.application.usecases.profile.outputs;

import com.kaua.monitoring.domain.profile.Profile;

public record CreateProfileOutput(
        String profileId,
        String username,
        String email,
        String avatarUrl,
        String type,
        String token
) {

    public static CreateProfileOutput from(final Profile aProfile, final String token) {
        return new CreateProfileOutput(
                aProfile.getId().getValue(),
                aProfile.getUsername(),
                aProfile.getEmail(),
                aProfile.getAvatarUrl(),
                aProfile.getType().name(),
                token
        );
    }
}
