package com.kaua.monitoring.application.gateways;

import com.kaua.monitoring.domain.profile.Resource;

public interface AvatarGateway {

    String create(String profileId, Resource resource);

    void deleteAvatar(String key);

    String findAvatarByKey(String key);

    void deleteByProfileId(String profileId);
}
