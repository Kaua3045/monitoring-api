package com.kaua.monitoring.infrastructure.link.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<LinkJpaEntity, String> {

    Page<LinkJpaEntity> findAllByProfileId(
            String profileId, Pageable page);
}
