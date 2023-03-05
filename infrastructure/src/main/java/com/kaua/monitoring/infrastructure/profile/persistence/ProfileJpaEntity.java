package com.kaua.monitoring.infrastructure.profile.persistence;

import com.kaua.monitoring.domain.profile.VersionAccountType;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "profiles")
@Entity
@Data
public class ProfileJpaEntity {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private VersionAccountType type;

    public ProfileJpaEntity() {}

    public ProfileJpaEntity(
            final String id,
            final String userId,
            final String username,
            final String email,
            final String avatarUrl,
            final VersionAccountType type
    ) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.type = type;
    }
}
