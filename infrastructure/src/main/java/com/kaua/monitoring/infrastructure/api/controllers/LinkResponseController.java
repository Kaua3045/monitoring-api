package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.infrastructure.api.LinkResponseAPI;
import com.kaua.monitoring.infrastructure.services.LinkResponseService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinkResponseController implements LinkResponseAPI {

    private final LinkResponseService linkResponseService;

    public LinkResponseController(final LinkResponseService linkResponseService) {
        this.linkResponseService = linkResponseService;
    }

    @Override
    public Pagination<LinkResponseOutput> getAllLinkResponseByUrlId(
            String urlId,
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        return this.linkResponseService.getAllLinkResponseByUrlId(
                urlId,
                search,
                page,
                perPage,
                sort,
                direction
        );
    }
}
