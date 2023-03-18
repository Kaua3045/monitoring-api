package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(value = "/profile")
public interface ProfileAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> create(@RequestHeader String authorization, @RequestBody CreateProfileBody body);

    @GetMapping(
            value = "{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> getByUserId(@PathVariable String userId);

    @PutMapping(
            value = "{profileId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> updateProfile(
            @PathVariable String profileId,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "avatar_url", required = false) MultipartFile avatarFile,
            @RequestParam(name = "type", required = false) String type
    );

    @DeleteMapping(value = "{profileId}")
    ResponseEntity<?> deleteById(@PathVariable String profileId);
}
