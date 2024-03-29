package com.kaua.monitoring.infrastructure.api.controllers;

import com.kaua.monitoring.domain.profile.Resource;
import com.kaua.monitoring.infrastructure.api.ProfileAPI;
import com.kaua.monitoring.infrastructure.exceptions.ImageSizeNotValidException;
import com.kaua.monitoring.infrastructure.exceptions.ImageTypeNotValidException;
import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import com.kaua.monitoring.infrastructure.services.ProfileService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

@RestController
public class ProfileController implements ProfileAPI {

    private final ProfileService profileService;
    private Bucket bucket;

    public ProfileController(final ProfileService profileService) {
        this.profileService = profileService;
        Bandwidth limit = Bandwidth.classic(15, Refill.greedy(15, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    public ResponseEntity<?> create(CreateProfileBody body) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(this.profileService.createProfile(body));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @Override
    public ResponseEntity<?> getByProfileId(String profileId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.profileService.getByProfileId(profileId));
    }

    @Override
    public ResponseEntity<?> getProfileByToken(String authorization) {
        final var tokenWithoutType = authorization.substring(7);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.profileService.getProfileByToken(tokenWithoutType));
    }

    @Override
    public ResponseEntity<?> updateProfile(
            String profileId,
            String username,
            String password,
            MultipartFile avatarFile,
            String type
    ) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(this.profileService.updateProfile(profileId, username, password, resourceOf(avatarFile), type));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
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
