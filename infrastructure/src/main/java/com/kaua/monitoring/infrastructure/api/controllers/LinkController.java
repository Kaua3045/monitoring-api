package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.infrastructure.api.LinkAPI;
import com.kaua.monitoring.infrastructure.link.inputs.CreateLinkBody;
import com.kaua.monitoring.infrastructure.link.inputs.UpdateLinkBody;
import com.kaua.monitoring.infrastructure.services.LinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinkController implements LinkAPI {

    private final LinkService linkService;

    public LinkController(final LinkService linkService) {
        this.linkService = linkService;
    }

    @Override
    public ResponseEntity<?> create(CreateLinkBody body) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.linkService.createLink(body));
    }

    @Override
    public ResponseEntity<?> getLinkById(String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.linkService.getLinkById(id));
    }

    @Override
    public Pagination<LinkOutput> getAllLinkByProfileId(
            String profileId,
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        return this.linkService.getAllLinkByProfileId(
                profileId,
                search,
                page,
                perPage,
                sort,
                direction
        );
    }

    @Override
    public ResponseEntity<?> updateLinkById(String id, UpdateLinkBody body) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.linkService.updateLink(id, body));
    }

    @Override
    public ResponseEntity<?> deleteLinkById(String id) {
        this.linkService.deleteLinkById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
