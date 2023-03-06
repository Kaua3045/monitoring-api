package com.kaua.monitoring.infrastructure.link.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepository extends JpaRepository<LinkJpaEntity, String> {

    List<LinkJpaEntity> findAllByProfileId(String profileId);
}
