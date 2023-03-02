package com.kaua.monitoring.infrastructure.profile.persistence;

import com.kaua.monitoring.domain.profile.VersionAccountType;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "tb_profile")
@Entity
@Data
public class ProfileJpaEntity {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private VersionAccountType type;

    public ProfileJpaEntity() {}

    public ProfileJpaEntity(
            final String id,
            final String userId,
            final String avatarUrl,
            final VersionAccountType type
    ) {
        this.id = id;
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.type = type;
    }
}
