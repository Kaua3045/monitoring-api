package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/profile")
public interface ProfileAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> create(@RequestHeader String authorization, @RequestBody CreateProfileBody body);

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> getByUserId(@PathVariable String userId);
}
