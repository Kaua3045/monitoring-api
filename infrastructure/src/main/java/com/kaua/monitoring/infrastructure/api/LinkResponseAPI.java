package com.kaua.monitoring.infrastructure.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/links-responses")
public interface LinkResponseAPI {

    @GetMapping(
            value = "{urlId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> getAllLinksByUrlId(@PathVariable String urlId);
}
