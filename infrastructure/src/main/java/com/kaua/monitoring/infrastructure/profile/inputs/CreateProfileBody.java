package com.kaua.monitoring.infrastructure.profile.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateProfileBody(
        @JsonProperty("username") String username,
        @JsonProperty("email") String email,
        @JsonProperty("password") String password,
        @JsonProperty("avatar_url") String avatarUrl
) {
}
