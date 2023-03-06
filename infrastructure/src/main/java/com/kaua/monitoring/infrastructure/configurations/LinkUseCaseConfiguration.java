package com.kaua.monitoring.infrastructure.configurations;

import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.link.create.CreateLinkUseCase;
import com.kaua.monitoring.application.usecases.link.create.DefaultCreateLinkUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkUseCaseConfiguration {

    private final LinkGateway linkGateway;
    private final ProfileGateway profileGateway;

    public LinkUseCaseConfiguration(final LinkGateway linkGateway, final ProfileGateway profileGateway) {
        this.linkGateway = linkGateway;
        this.profileGateway = profileGateway;
    }

    @Bean
    public CreateLinkUseCase createLinkUseCase() {
        return new DefaultCreateLinkUseCase(linkGateway, profileGateway);
    }
}
