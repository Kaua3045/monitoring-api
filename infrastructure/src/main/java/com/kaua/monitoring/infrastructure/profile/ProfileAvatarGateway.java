package com.kaua.monitoring.infrastructure.profile;

import com.kaua.monitoring.application.gateways.AvatarGateway;
import com.kaua.monitoring.domain.profile.Resource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Component
public class ProfileAvatarGateway implements AvatarGateway {

    private static final String BUCKET_NAME = "monitoring-avatar-bucket";

    @Override
    public String create(String profileId, Resource resource) {
        try (final var s3Client = S3Client.create()) {

            final var KEY_WITH_PREFIX = profileId + "-" + resource.getName();

            final var avatarAlreadyExistsRequest = ListObjectsRequest.builder()
                    .bucket(BUCKET_NAME)
                    .prefix(profileId)
                    .build();

            final var avatarAlreadyExists = s3Client.listObjects(avatarAlreadyExistsRequest);

            if (avatarAlreadyExists != null && avatarAlreadyExists.prefix().equalsIgnoreCase(profileId)) {
                avatarAlreadyExists.contents()
                                .forEach(image -> deleteAvatar(image.key()));
            }

            final var request = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(KEY_WITH_PREFIX)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(resource.getContentType())
                    .build();

            s3Client.putObject(request,
                    RequestBody
                            .fromInputStream(
                                    resource.getInputStream(),
                                    resource.getInputStream().available()
                            ));

            return findAvatarByKey(KEY_WITH_PREFIX);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void deleteAvatar(String key) {
        try(final var s3Client = S3Client.create()) {
            final var request = DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();

            s3Client.deleteObject(request);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public String findAvatarByKey(String key) {
        try(final var s3Client = S3Client.create()) {
            final var aGetUrlAvatar = GetUrlRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();

            return s3Client.utilities().getUrl(aGetUrlAvatar).toString();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void deleteByProfileId(String profileId) {
        try(final var s3Client = S3Client.create()) {
            final var avatarAlreadyExistsRequest = ListObjectsRequest.builder()
                    .bucket(BUCKET_NAME)
                    .prefix(profileId)
                    .build();

            final var avatarAlreadyExists = s3Client.listObjects(avatarAlreadyExistsRequest);

            if (!avatarAlreadyExists.contents().isEmpty()) {
                avatarAlreadyExists.contents().forEach(image -> deleteAvatar(image.key()));
            }
        } catch (Throwable t) {
            throw new RuntimeException(t.getMessage());
        }
    }
}
