package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import com.kaua.monitoring.infrastructure.api.LinkResponseAPI;
import com.kaua.monitoring.infrastructure.services.LinkResponseService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LinkResponseController implements LinkResponseAPI {

    private final LinkResponseService linkResponseService;

    public LinkResponseController(final LinkResponseService linkResponseService) {
        this.linkResponseService = linkResponseService;
    }

    @Override
    public List<LinkResponseOutput> getAllLinkResponseByUrlId(String urlId) {
        return this.linkResponseService.getFirst90LinkResponseByUrlId(urlId);
    }
}
