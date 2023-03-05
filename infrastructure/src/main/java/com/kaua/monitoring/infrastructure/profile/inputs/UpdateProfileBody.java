package com.kaua.monitoring.infrastructure.profile.inputs;

public record UpdateProfileBody(
        String username,
        String avatarUrl,
        String type
) {
}
