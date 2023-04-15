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

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private VersionAccountType type;

    public ProfileJpaEntity() {}

    public ProfileJpaEntity(
            final String id,
            final String username,
            final String email,
            final String password,
            final String avatarUrl,
            final VersionAccountType type
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.type = type;
    }
}
