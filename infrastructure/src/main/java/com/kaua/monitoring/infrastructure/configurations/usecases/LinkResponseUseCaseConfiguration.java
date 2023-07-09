package com.kaua.monitoring.infrastructure.configurations.usecases;

import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.gateways.LinkResponseGateway;
import com.kaua.monitoring.application.usecases.checking.retrieve.DefaultListLinkResponseByUrlIdUseCase;
import com.kaua.monitoring.application.usecases.checking.retrieve.ListLinkResponseByUrlIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class LinkResponseUseCaseConfiguration {

    private final LinkResponseGateway linkResponseGateway;
    private final LinkGateway linkGateway;

    public LinkResponseUseCaseConfiguration(
            final LinkResponseGateway linkResponseGateway,
            final LinkGateway linkGateway
    ) {
        this.linkResponseGateway = Objects.requireNonNull(linkResponseGateway);
        this.linkGateway = Objects.requireNonNull(linkGateway);
    }

    @Bean
    public ListLinkResponseByUrlIdUseCase listLinkResponseByUrlIdUseCase() {
        return new DefaultListLinkResponseByUrlIdUseCase(linkResponseGateway, linkGateway);
    }
}
