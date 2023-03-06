package com.kaua.monitoring.infrastructure.link;

import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaFactory;
import com.kaua.monitoring.infrastructure.link.persistence.LinkRepository;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public List<Link> findAllByProfileId(String profileId) {
        return this.linkRepository.findAllByProfileId(profileId)
                .stream().map(LinkJpaFactory::toDomain)
                .toList();
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
