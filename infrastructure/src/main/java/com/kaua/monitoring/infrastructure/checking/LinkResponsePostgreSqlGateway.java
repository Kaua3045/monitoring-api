package com.kaua.monitoring.infrastructure.checking;

import com.kaua.monitoring.application.gateways.LinkResponseGateway;
import com.kaua.monitoring.domain.checking.LinkResponse;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.domain.pagination.SearchQuery;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseJpaEntity;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseJpaFactory;
import com.kaua.monitoring.infrastructure.checking.persistence.LinkResponseRepository;
import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaEntity;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

@Component
public class LinkResponsePostgreSqlGateway implements LinkResponseGateway {

    private final LinkResponseRepository linkResponseRepository;

    public LinkResponsePostgreSqlGateway(final LinkResponseRepository linkResponseRepository) {
        this.linkResponseRepository = linkResponseRepository;
    }

    @Override
    public Pagination<LinkResponse> findAllByUrlId(String urlId, SearchQuery aQuery) {
        final var pageResult = this.linkResponseWhereUrlId(urlId, aQuery);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(LinkResponseJpaFactory::toDomain).toList()
        );
    }

    private Page<LinkResponseJpaEntity> linkResponseWhereUrlId(final String urlId, final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher(
                        "responseMessage",
                        ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("statusCode");

        final var aLinkResponse = new LinkResponseJpaEntity();
        final var aLink = new LinkJpaEntity();

        aLink.setId(urlId);
        aLinkResponse.setLink(aLink);
        aLinkResponse.setResponseMessage(aQuery.terms());

        final var exampleWithValuesAndMatcher = Example.of(aLinkResponse, matcher);

        final var pageResult = this.linkResponseRepository.findAll(exampleWithValuesAndMatcher, page);

        return pageResult;
    }
}
