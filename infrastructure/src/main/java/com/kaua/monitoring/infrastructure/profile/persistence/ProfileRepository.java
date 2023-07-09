package com.kaua.monitoring.infrastructure.profile.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileJpaEntity, String> {

    Optional<ProfileJpaEntity> findByEmail(final String email);
}
