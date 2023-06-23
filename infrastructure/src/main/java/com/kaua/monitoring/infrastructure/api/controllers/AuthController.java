package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.infrastructure.api.AuthAPI;
import com.kaua.monitoring.infrastructure.profile.inputs.AuthenticateProfileBody;
import com.kaua.monitoring.infrastructure.services.AuthenticateService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class AuthController implements AuthAPI {

    private final AuthenticateService authenticateService;
    private Bucket bucket;

    public AuthController(final AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
        Bandwidth limit = Bandwidth.classic(15, Refill.greedy(15, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    public ResponseEntity<?> authenticate(AuthenticateProfileBody input) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.authenticateService.authenticate(input));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
