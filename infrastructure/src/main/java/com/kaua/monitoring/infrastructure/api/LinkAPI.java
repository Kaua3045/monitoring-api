package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.infrastructure.link.inputs.CreateLinkBody;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/links")
public interface LinkAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> create(@RequestBody CreateLinkBody body);

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> getLinkById(@PathVariable String id);

    @GetMapping(
            value = "/list/{profileId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    Pagination<LinkOutput> getAllLinkByProfileId(
            @PathVariable String profileId,
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "title") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
            );

    @DeleteMapping(value = "{id}")
    ResponseEntity<?> deleteLinkById(@PathVariable String id);
}
