package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.infrastructure.api.LinkAPI;
import com.kaua.monitoring.infrastructure.link.inputs.CreateLinkBody;
import com.kaua.monitoring.infrastructure.link.inputs.UpdateLinkBody;
import com.kaua.monitoring.infrastructure.services.LinkService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class LinkController implements LinkAPI {

    private final LinkService linkService;
    private Bucket bucket;

    public LinkController(final LinkService linkService) {
        this.linkService = linkService;
        Bandwidth limit = Bandwidth.classic(15, Refill.greedy(15, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    public ResponseEntity<?> create(CreateLinkBody body) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(this.linkService.createLink(body));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @Override
    public ResponseEntity<?> getLinkById(String id) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(this.linkService.getLinkById(id));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
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
        if (bucket.tryConsume(1)) {
            this.linkService.deleteLinkById(id);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
