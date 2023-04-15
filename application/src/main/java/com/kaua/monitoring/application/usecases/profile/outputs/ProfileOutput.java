package com.kaua.monitoring.application.usecases.profile.outputs;

import com.kaua.monitoring.domain.profile.Profile;

public record ProfileOutput(
        String profileId,
        String username,
        String email,
        String avatarUrl,
        String type
) {

    public static ProfileOutput from(final Profile aProfile) {
        return new ProfileOutput(
                aProfile.getId().getValue(),
                aProfile.getUsername(),
                aProfile.getEmail(),
                aProfile.getAvatarUrl(),
                aProfile.getType().name()
        );
    }
}
