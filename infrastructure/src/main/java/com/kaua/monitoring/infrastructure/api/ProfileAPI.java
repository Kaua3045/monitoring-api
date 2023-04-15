package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(value = "/profile")
@Tag(name = "Profiles")
public interface ProfileAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "400", description = "Domain exception validation thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> create(@RequestBody CreateProfileBody body);

    @GetMapping(
            value = "{profileId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a profile by profile id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile find successfully"),
            @ApiResponse(responseCode = "404", description = "Profile was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> getByProfileId(@PathVariable String profileId);

    @GetMapping(
            path = "me",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a profile by token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile find sucessfully"),
            @ApiResponse(responseCode = "404", description = "Profile was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> getProfileByToken(@RequestHeader String authorization);

    @PutMapping(
            value = "{profileId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a profile by profile id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Domain exception thrown"),
            @ApiResponse(responseCode = "404", description = "Profile was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updateProfile(
            @PathVariable String profileId,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "avatar_url", required = false) MultipartFile avatarFile,
            @RequestParam(name = "type", required = false) String type
    );

    @DeleteMapping(value = "{profileId}")
    @Operation(summary = "Delete a profile by profile id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile deleted successfully"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> deleteById(@PathVariable String profileId);
}
