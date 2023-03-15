package com.kaua.monitoring.infrastructure.checking;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkResponseGateway;
import com.kaua.monitoring.domain.checking.LinkResponse;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseJpaFactory;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseRepository;
import com.kaua.monitoring.infrastructure.link.persistence.LinkRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LinkResponsePostgreSqlGateway implements LinkResponseGateway {

    private final LinkResponseRepository linkResponseRepository;
    private final LinkRepository linkRepository;

    public LinkResponsePostgreSqlGateway(
            final LinkResponseRepository linkResponseRepository,
            final LinkRepository linkRepository
    ) {
        this.linkResponseRepository = linkResponseRepository;
        this.linkRepository = linkRepository;
    }

    @Override
    public List<LinkResponse> findAllTop90(String urlId) {
        final var aLinkEntity = this.linkRepository.findById(urlId)
                .orElseThrow(() -> new NotFoundException(Link.class, urlId));

        return this.linkResponseRepository.findTop90ByUrlId(aLinkEntity)
                .stream().map(LinkResponseJpaFactory::toDomain).toList();
    }
}
