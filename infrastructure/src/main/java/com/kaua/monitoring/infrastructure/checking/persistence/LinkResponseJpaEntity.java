package com.kaua.monitoring.infrastructure.checking.persistence;

import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Table(name = "links_responses")
@Entity(name = "links_responses")
@Data
public class LinkResponseJpaEntity {

    @Id
    private String id;

    @Column(name = "response_message", nullable = false)
    private String responseMessage;

    @Column(name = "status_code", nullable = false)
    private int statusCode;

    @Column(name = "verified_date", nullable = false)
    private Instant verifiedDate;

    @ManyToOne
    @JoinColumn(name = "url_id", nullable = false)
    private LinkJpaEntity urlId;

    public LinkResponseJpaEntity() {}

    public LinkResponseJpaEntity(
            String id,
            String responseMessage,
            int statusCode,
            Instant verifiedDate,
            LinkJpaEntity urlId
    ) {
        this.id = id;
        this.responseMessage = responseMessage;
        this.statusCode = statusCode;
        this.verifiedDate = verifiedDate;
        this.urlId = urlId;
    }
}
