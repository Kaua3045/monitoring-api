package com.kaua.monitoring.infrastructure.profile.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateProfileBody(
        @JsonProperty("user_id") String userId,
        @JsonProperty("username") String username,
        @JsonProperty("email") String email,
        @JsonProperty("avatar_url") String avatarUrl
) {
}
