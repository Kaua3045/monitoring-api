package com.kaua.monitoring.infrastructure.profile.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileJpaEntity, String> {
}
