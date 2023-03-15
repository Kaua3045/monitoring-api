package com.kaua.monitoring.infrastructure.link.persistence;

import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Table(name = "links")
@Entity
@Data
public class LinkJpaEntity implements Serializable {

    @Id
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "execute_date", nullable = false)
    private Instant executeDate;

    @Enumerated(EnumType.STRING)
    private LinkExecutions linkExecution;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileJpaEntity profile;

    public LinkJpaEntity() {}

    public LinkJpaEntity(
            final String id,
            final String title,
            final String url,
            final Instant executeDate,
            final LinkExecutions linkExecution,
            final ProfileJpaEntity profile
    ) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.executeDate = executeDate;
        this.linkExecution = linkExecution;
        this.profile = profile;
    }
}
