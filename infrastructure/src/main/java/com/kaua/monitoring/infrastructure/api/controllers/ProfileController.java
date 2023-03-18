package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.domain.profile.Resource;
import com.kaua.monitoring.infrastructure.api.ProfileAPI;
import com.kaua.monitoring.infrastructure.exceptions.ImageSizeNotValidException;
import com.kaua.monitoring.infrastructure.exceptions.ImageTypeNotValidException;
import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import com.kaua.monitoring.infrastructure.services.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<?> updateProfile(
            String profileId,
            String username,
            MultipartFile avatarFile,
            String type
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.profileService.updateProfile(profileId, username, resourceOf(avatarFile), type));
    }

    @Override
    public ResponseEntity<?> deleteById(String profileId) {
        this.profileService.deleteProfile(profileId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) {
            return null;
        }

        isValidImage(part);

        try {
            return Resource.with(
                    part.getInputStream(),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void isValidImage(final MultipartFile part) {
        final var imageTypes = List.of(
                "image/jpeg",
                "image/jpg",
                "image/png"
        );

        final var validImageType = imageTypes.contains(part.getContentType());
        final var IMAGE_SIZE = 500000;

        if (!validImageType) {
            throw new ImageTypeNotValidException();
        }

        if (part.getSize() > IMAGE_SIZE) {
            throw new ImageSizeNotValidException();
        }
    }
}
