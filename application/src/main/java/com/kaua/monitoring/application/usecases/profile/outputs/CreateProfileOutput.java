package com.kaua.monitoring.application.usecases.profile.outputs;

public record CreateProfileOutput(
        String profileId,
        String userId,
        String avatarUrl,
        String type
) {
}
