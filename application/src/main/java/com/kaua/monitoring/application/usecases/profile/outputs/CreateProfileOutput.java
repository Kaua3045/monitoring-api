package com.kaua.monitoring.application.usecases.profile.outputs;

public record CreateProfileOutput(
        String profileId,
        String userId,
        String username,
        String email,
        String avatarUrl,
        String type
) {
}
