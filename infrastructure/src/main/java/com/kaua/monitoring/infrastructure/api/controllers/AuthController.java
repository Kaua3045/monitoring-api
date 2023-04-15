package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.infrastructure.api.AuthAPI;
import com.kaua.monitoring.infrastructure.profile.inputs.AuthenticateProfileBody;
import com.kaua.monitoring.infrastructure.services.AuthenticateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthAPI {

    private final AuthenticateService authenticateService;

    public AuthController(final AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @Override
    public ResponseEntity<?> authenticate(AuthenticateProfileBody input) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.authenticateService.authenticate(input));
    }
}
