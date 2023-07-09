package com.kaua.monitoring.infrastructure.link.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<LinkJpaEntity, String> {
}
