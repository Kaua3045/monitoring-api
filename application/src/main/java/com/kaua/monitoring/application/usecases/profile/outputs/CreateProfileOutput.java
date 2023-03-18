package com.kaua.monitoring.application.usecases.profile.outputs;

import com.kaua.monitoring.domain.profile.Profile;

public record CreateProfileOutput(
        String profileId,
        String userId,
        String username,
        String email,
        String avatarUrl,
        String type
) {

    public static CreateProfileOutput from(final Profile aProfile) {
        return new CreateProfileOutput(
                aProfile.getId().getValue(),
                aProfile.getUserId(),
                aProfile.getUsername(),
                aProfile.getEmail(),
                aProfile.getAvatarUrl(),
                aProfile.getType().name()
        );
    }
}
