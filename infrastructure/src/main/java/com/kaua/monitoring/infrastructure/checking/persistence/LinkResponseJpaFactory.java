package com.kaua.monitoring.infrastructure.checking.persistence;

import com.kaua.monitoring.domain.checking.LinkResponse;
import com.kaua.monitoring.domain.checking.LinkResponseID;
import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaFactory;

public final class LinkResponseJpaFactory {

    private LinkResponseJpaFactory() {}

    public static LinkResponseJpaEntity toEntity(final LinkResponse aLinkResponse) {
        return new LinkResponseJpaEntity(
                aLinkResponse.getId().getValue(),
                aLinkResponse.getResponseMessage(),
                aLinkResponse.getStatusCode(),
                aLinkResponse.getVerifiedDate(),
                LinkJpaFactory.toEntity(aLinkResponse.getLink())
        );
    }

    public static LinkResponse toDomain(final LinkResponseJpaEntity aLinkResponse) {
        return new LinkResponse(
                LinkResponseID.from(aLinkResponse.getId()),
                aLinkResponse.getResponseMessage(),
                aLinkResponse.getStatusCode(),
                aLinkResponse.getVerifiedDate(),
                LinkJpaFactory.toDomain(aLinkResponse.getUrlId())
        );
    }
}
