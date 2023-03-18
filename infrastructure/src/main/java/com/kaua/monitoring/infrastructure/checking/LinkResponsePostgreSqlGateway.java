package com.kaua.monitoring.infrastructure.checking;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkResponseGateway;
import com.kaua.monitoring.domain.checking.LinkResponse;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseJpaFactory;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseRepository;
import com.kaua.monitoring.infrastructure.link.persistence.LinkRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
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
    public List<LinkResponse> findAllByUrlIdAndFilterByVerifiedDate(
            final String urlId,
            final Instant startTimestamp,
            final Instant endTimestamp
    ) {
        final var aLinkEntity = this.linkRepository.findById(urlId)
                .orElseThrow(() -> new NotFoundException(Link.class, urlId));

        return this.linkResponseRepository.findByUrlIdAndVerifiedDateBetween(
                aLinkEntity,
                startTimestamp,
                endTimestamp
        ).stream().map(LinkResponseJpaFactory::toDomain).toList();
    }
}
