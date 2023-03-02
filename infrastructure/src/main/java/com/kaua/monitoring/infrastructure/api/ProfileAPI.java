package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(name = "/profile")
public interface ProfileAPI {

    @PostMapping
    ResponseEntity<?> create(@RequestBody CreateProfileBody body);
}
