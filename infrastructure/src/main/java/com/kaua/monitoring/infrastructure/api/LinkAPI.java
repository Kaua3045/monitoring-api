package com.kaua.monitoring.infrastructure.api;

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
}
