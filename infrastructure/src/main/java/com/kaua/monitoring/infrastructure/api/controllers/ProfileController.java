package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.infrastructure.api.ProfileAPI;
import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import com.kaua.monitoring.infrastructure.profile.inputs.UpdateProfileBody;
import com.kaua.monitoring.infrastructure.services.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController implements ProfileAPI {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public ResponseEntity<?> create(String authorization, CreateProfileBody body) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.profileService.createProfile(authorization, body));
    }

    @Override
    public ResponseEntity<?> getByUserId(String userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.profileService.getByUserIdProfile(userId));
    }

    @Override
    public ResponseEntity<?> updateProfile(String profileId, UpdateProfileBody body) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.profileService.updateProfile(profileId, body));
    }

    @Override
    public ResponseEntity<?> deleteById(String profileId) {
        this.profileService.deleteProfile(profileId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
