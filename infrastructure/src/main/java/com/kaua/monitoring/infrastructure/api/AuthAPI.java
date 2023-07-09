package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.infrastructure.profile.inputs.AuthenticateProfileBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/auth")
@Tag(name = "Authentication")
public interface AuthAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Authenticate in application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticate successfully"),
            @ApiResponse(responseCode = "400", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> authenticate(@RequestBody AuthenticateProfileBody input);
}
