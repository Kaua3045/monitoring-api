package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import com.kaua.monitoring.domain.pagination.Pagination;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "/links-responses")
public interface LinkResponseAPI {

    @GetMapping(
            value = "{urlId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    Pagination<LinkResponseOutput> getAllLinkResponseByUrlId(
            @PathVariable String urlId,
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "responseMessage") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
            );
}
