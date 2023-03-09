package com.kaua.monitoring.infrastructure.link.responses;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkResponseRepository extends JpaRepository<LinkResponseJpaEntity, String> {

    List<LinkResponseJpaEntity> findAllByUrlId(String urlId);
}
