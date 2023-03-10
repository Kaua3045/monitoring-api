package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.infrastructure.api.LinkAPI;
import com.kaua.monitoring.infrastructure.link.inputs.CreateLinkBody;
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
    public ResponseEntity<?> deleteLinkById(String id) {
        this.linkService.deleteLinkById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
