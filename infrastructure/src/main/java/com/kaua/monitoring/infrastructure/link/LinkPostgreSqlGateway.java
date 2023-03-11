package com.kaua.monitoring.infrastructure.link;

import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.domain.pagination.SearchQuery;
import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaEntity;
import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaFactory;
import com.kaua.monitoring.infrastructure.link.persistence.LinkRepository;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaEntity;
import org.springframework.data.domain.*;
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
        final var pageResult = this.linksWhereProfileId(profileId, aQuery);

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

    private Page<LinkJpaEntity> linksWhereProfileId(String profileId, SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher(
                        "title",
                        ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        final var aLink = new LinkJpaEntity();
        final var aProfile = new ProfileJpaEntity();

        aProfile.setId(profileId);
        aLink.setProfile(aProfile);
        aLink.setTitle(aQuery.terms());

        final var exampleWithValuesAndMatcher = Example.of(aLink, matcher);

        final var pageResult = this.linkRepository.findAll(exampleWithValuesAndMatcher, page);

        return pageResult;
    }
}
