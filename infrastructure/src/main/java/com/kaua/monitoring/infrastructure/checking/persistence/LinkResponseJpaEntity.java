package com.kaua.monitoring.infrastructure.checking.persistence;

import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaEntity;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "links_responses")
@Entity
@Data
public class LinkResponseJpaEntity {

    @Id
    private String id;

    @Column(name = "response_message", nullable = false)
    private String responseMessage;

    @Column(name = "status_code", nullable = false)
    private int statusCode;

    @ManyToOne
    @JoinColumn(name = "url_id", nullable = false)
    private LinkJpaEntity urlId;

    public LinkResponseJpaEntity() {}

    public LinkResponseJpaEntity(
            String id,
            String responseMessage,
            int statusCode,
            LinkJpaEntity urlId
    ) {
        this.id = id;
        this.responseMessage = responseMessage;
        this.statusCode = statusCode;
        this.urlId = urlId;
    }
}
