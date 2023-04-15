package com.kaua.monitoring.application.gateways;

import com.kaua.monitoring.domain.profile.Profile;

import java.util.Optional;

public interface ProfileGateway {

    Profile create(final Profile aProfile);

    Optional<Profile> findById(final String profileId);

    Optional<Profile> findByEmail(final String email);

    Profile update(final Profile aProfile);

    void deleteById(final String profileId);
}
