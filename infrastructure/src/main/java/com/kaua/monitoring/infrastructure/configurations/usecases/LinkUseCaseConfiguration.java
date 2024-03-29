package com.kaua.monitoring.infrastructure.configurations.usecases;

import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.link.create.CreateLinkUseCase;
import com.kaua.monitoring.application.usecases.link.create.DefaultCreateLinkUseCase;
import com.kaua.monitoring.application.usecases.link.delete.DefaultDeleteLinkUseCase;
import com.kaua.monitoring.application.usecases.link.delete.DeleteLinkUseCase;
import com.kaua.monitoring.application.usecases.link.retrieve.get.DefaultGetLinkByIdUseCase;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdUseCase;
import com.kaua.monitoring.application.usecases.link.retrieve.list.profileId.DefaultListLinkByProfileIdUseCase;
import com.kaua.monitoring.application.usecases.link.retrieve.list.profileId.ListLinkByProfileIdUseCase;
import com.kaua.monitoring.application.usecases.link.update.DefaultUpdateLinkUseCase;
import com.kaua.monitoring.application.usecases.link.update.UpdateLinkUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class LinkUseCaseConfiguration {

    private final LinkGateway linkGateway;
    private final ProfileGateway profileGateway;

    public LinkUseCaseConfiguration(final LinkGateway linkGateway, final ProfileGateway profileGateway) {
        this.linkGateway = Objects.requireNonNull(linkGateway);
        this.profileGateway = Objects.requireNonNull(profileGateway);
    }

    @Bean
    public CreateLinkUseCase createLinkUseCase() {
        return new DefaultCreateLinkUseCase(linkGateway, profileGateway);
    }

    @Bean
    public GetLinkByIdUseCase getLinkByIdUseCase() {
        return new DefaultGetLinkByIdUseCase(linkGateway);
    }

    @Bean
    public ListLinkByProfileIdUseCase listLinkByProfileIdUseCase() {
        return new DefaultListLinkByProfileIdUseCase(linkGateway, profileGateway);
    }

    @Bean
    public UpdateLinkUseCase updateLinkUseCase() {
        return new DefaultUpdateLinkUseCase(linkGateway);
    }

    @Bean
    public DeleteLinkUseCase deleteLinkUseCase() {
        return new DefaultDeleteLinkUseCase(linkGateway);
    }
}
