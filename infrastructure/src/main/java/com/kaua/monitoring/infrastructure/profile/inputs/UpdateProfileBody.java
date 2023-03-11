package com.kaua.monitoring.infrastructure.profile.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateProfileBody(
        @JsonProperty("username") String username,
        @JsonProperty("avatar_url") String avatarUrl,
        @JsonProperty("type") String type
) {
}
