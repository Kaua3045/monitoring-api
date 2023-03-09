package com.kaua.monitoring.infrastructure.link.responses;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LinkResponsePostgreSqlGateway {

    private final LinkResponseRepository linkResponseRepository;

    public LinkResponsePostgreSqlGateway(final LinkResponseRepository linkResponseRepository) {
        this.linkResponseRepository = linkResponseRepository;
    }

    public List<LinkResponseJpaEntity> findAllLinksByUrlId(String urlId) {
        return this.linkResponseRepository.findAllByUrlId(urlId);
    }
}
