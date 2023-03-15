package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import com.kaua.monitoring.application.usecases.checking.retrieve.ListLinkResponseByUrlIdCommand;
import com.kaua.monitoring.application.usecases.checking.retrieve.ListLinkResponseByUrlIdUseCase;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.domain.pagination.SearchQuery;
import org.springframework.stereotype.Component;

@Component
public class LinkResponseService {

    private final ListLinkResponseByUrlIdUseCase listLinkResponseByUrlIdUseCase;

    public LinkResponseService(final ListLinkResponseByUrlIdUseCase listLinkResponseByUrlIdUseCase) {
        this.listLinkResponseByUrlIdUseCase = listLinkResponseByUrlIdUseCase;
    }

    public Pagination<LinkResponseOutput> getAllLinkResponseByUrlId(
            String urlId,
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        final var aCommand = new ListLinkResponseByUrlIdCommand(
                urlId,
                new SearchQuery(page, perPage, search, sort, direction)
        );

        return this.listLinkResponseByUrlIdUseCase.execute(aCommand);
    }
}
