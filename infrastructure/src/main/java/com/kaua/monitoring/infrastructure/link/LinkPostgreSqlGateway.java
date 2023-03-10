package com.kaua.monitoring.infrastructure.link;

import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.domain.pagination.SearchQuery;
import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaFactory;
import com.kaua.monitoring.infrastructure.link.persistence.LinkRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LinkPostgreSqlGateway implements LinkGateway {

    private final LinkRepository linkRepository;

    public LinkPostgreSqlGateway(final LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public Link create(Link aLink) {
        final var aLinkEntity = this.linkRepository.save(LinkJpaFactory.toEntity(aLink));
        return LinkJpaFactory.toDomain(aLinkEntity);
    }

    @Override
    public Optional<Link> findById(String id) {
        return this.linkRepository.findById(id)
                .map(LinkJpaFactory::toDomain);
    }

    @Override
    public Pagination<Link> findAllByProfileId(String profileId, SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var pageResult = this.linkRepository.findAllByProfileId(
                profileId,
                page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(LinkJpaFactory::toDomain).toList()
        );
    }

    @Override
    public Link update(Link aLink) {
        final var aLinkEntity = this.linkRepository.save(LinkJpaFactory.toEntity(aLink));
        return LinkJpaFactory.toDomain(aLinkEntity);
    }

    @Override
    public void deleteById(String id) {
        if (this.linkRepository.existsById(id)) {
            this.linkRepository.deleteById(id);
        }
    }
}
