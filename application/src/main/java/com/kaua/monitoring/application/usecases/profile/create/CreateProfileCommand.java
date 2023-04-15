package com.kaua.monitoring.application.usecases.profile.create;

public record CreateProfileCommand(
        String username,
        String email,
        String password,
        String avatarUrl
) {
}
