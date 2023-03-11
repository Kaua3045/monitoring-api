package com.kaua.monitoring.infrastructure.checking.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkResponseRepository extends JpaRepository<LinkResponseJpaEntity, String> {
}
