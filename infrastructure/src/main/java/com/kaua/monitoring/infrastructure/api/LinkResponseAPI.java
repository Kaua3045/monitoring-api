package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/links-responses")
public interface LinkResponseAPI {

    @GetMapping(
            value = "{urlId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    List<LinkResponseOutput> getAllLinkResponseByUrlId(@PathVariable String urlId);
}
