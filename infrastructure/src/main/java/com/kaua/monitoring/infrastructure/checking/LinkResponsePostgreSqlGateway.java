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
//        Hibernate: select l1_0.id,l1_0.execute_date,l1_0.link_execution,l1_0.profile_id,l1_0.title,l1_0.url from links l1_0 where lower(l1_0.title) like ? escape '\' and l1_0.profile_id=? order by l1_0.title asc offset ? rows fetch first ? rows only
//        Hibernate: select l1_0.id,l1_0.url_id,l1_0.response_message,l1_0.status_code from links_responses l1_0 where l1_0.status_code=? and lower(l1_0.response_message) like ? escape '\' and l1_0.url_id=? order by l1_0.response_message asc offset ? rows fetch first ? rows only
        final var exampleWithValuesAndMatcher = Example.of(aLinkResponse, matcher);

        final var pageResult = this.linkResponseRepository.findAll(exampleWithValuesAndMatcher, page);
        System.out.println(pageResult);
        return pageResult;
    }
}
