package com.kaua.monitoring.infrastructure.link.responses;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "links_responses")
@Entity
@Data
public class LinkResponseJpaEntity {

    @Id
    private String id;

    @Column(name = "urlid", nullable = false)
    private String urlId;

    @Column(name = "response_message", nullable = false)
    private String responseMessage;

    @Column(name = "status_code", nullable = false)
    private int statusCode;

    public LinkResponseJpaEntity() {}

    public LinkResponseJpaEntity(String id, String urlId, String responseMessage, int statusCode) {
        this.id = id;
        this.urlId = urlId;
        this.responseMessage = responseMessage;
        this.statusCode = statusCode;
    }
}
