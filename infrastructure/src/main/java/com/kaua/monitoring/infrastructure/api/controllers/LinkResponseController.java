package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.infrastructure.api.LinkResponseAPI;
import com.kaua.monitoring.infrastructure.link.responses.LinkResponsePostgreSqlGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinkResponseController implements LinkResponseAPI {

    @Autowired
    private LinkResponsePostgreSqlGateway linkResponsePostgreSqlGateway;

    @Override
    public ResponseEntity<?> getAllLinksByUrlId(String urlId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(linkResponsePostgreSqlGateway.findAllLinksByUrlId(urlId));
    }
}
