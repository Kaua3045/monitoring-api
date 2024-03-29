package com.kaua.monitoring.application.usecases.profile.update;

import com.kaua.monitoring.domain.profile.Resource;

public record UpdateProfileCommand(
        String profileId,
        String username,
        String password,
        Resource avatarUrl,
        String type
) {
}
