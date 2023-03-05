package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import com.kaua.monitoring.infrastructure.profile.inputs.UpdateProfileBody;
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
            value = "{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> getByUserId(@PathVariable String userId);

    @PutMapping(
            value = "{profileId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> updateProfile(@PathVariable String profileId, @RequestBody UpdateProfileBody body);

    @DeleteMapping(value = "{profileId}")
    ResponseEntity<?> deleteById(@PathVariable String profileId);
}
