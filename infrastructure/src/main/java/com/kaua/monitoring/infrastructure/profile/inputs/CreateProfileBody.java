package com.kaua.monitoring.infrastructure.profile.inputs;

public record CreateProfileBody(
        String userId,
        String username,
        String email,
        String avatarUrl
) {
}
