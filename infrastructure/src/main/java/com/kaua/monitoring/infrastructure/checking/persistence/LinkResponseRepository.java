package com.kaua.monitoring.infrastructure.checking.persistence;

import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface LinkResponseRepository extends JpaRepository<LinkResponseJpaEntity, String> {

    List<LinkResponseJpaEntity> findByUrlIdAndVerifiedDateBetween(LinkJpaEntity urlId, Instant startTimestamp, Instant endTimestamp);
}
