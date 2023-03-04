package com.kaua.monitoring.application.usecases.profile.update;

public record UpdateProfileCommand(String profileId, String username, String avatarUrl, String type) {
}
