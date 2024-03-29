package com.kaua.monitoring.infrastructure.link.persistence;

import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkID;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaFactory;

public final class LinkJpaFactory {

    private LinkJpaFactory() {}

    public static LinkJpaEntity toEntity(final Link aLink) {
        return new LinkJpaEntity(
                aLink.getId().getValue(),
                aLink.getTitle(),
                aLink.getUrl(),
                aLink.getExecuteDate(),
                aLink.getNextExecuteDate(),
                aLink.getLinkExecution(),
                ProfileJpaFactory.toEntity(aLink.getProfile())
        );
    }

    public static Link toDomain(final LinkJpaEntity aLinkEntity) {
        return new Link(
                LinkID.from(aLinkEntity.getId()),
                aLinkEntity.getTitle(),
                aLinkEntity.getUrl(),
                aLinkEntity.getExecuteDate(),
                aLinkEntity.getNextExecuteDate(),
                aLinkEntity.getLinkExecution(),
                ProfileJpaFactory.toDomain(aLinkEntity.getProfile())
        );
    }
}
