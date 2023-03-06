package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.link.create.CreateLinkCommand;
import com.kaua.monitoring.application.usecases.link.create.CreateLinkUseCase;
import com.kaua.monitoring.application.usecases.link.outputs.CreateLinkOutput;
import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdCommand;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdUseCase;
import com.kaua.monitoring.infrastructure.link.inputs.CreateLinkBody;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class LinkService {

    private final CreateLinkUseCase createLinkUseCase;
    private final GetLinkByIdUseCase getLinkByIdUseCase;

    public LinkService(final CreateLinkUseCase createLinkUseCase, final GetLinkByIdUseCase getLinkByIdUseCase) {
        this.createLinkUseCase = createLinkUseCase;
        this.getLinkByIdUseCase = getLinkByIdUseCase;
    }

    public CreateLinkOutput createLink(CreateLinkBody body) {
        final var executeDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(body.executeDate()),
                ZoneId.of("America/Sao_Paulo")
        );

        final var aCommand = new CreateLinkCommand(
                body.title(),
                body.url(),
                executeDate,
                body.repeat(),
                body.profileId()
        );

        final var aResult = this.createLinkUseCase.execute(aCommand);

        if (aResult.isLeft()) {
            throw aResult.getLeft();
        }

        return aResult.getRight();
    }

    public LinkOutput getLinkById(String id) {
        final var aCommand = new GetLinkByIdCommand(id);
        return this.getLinkByIdUseCase.execute(aCommand);
    }
}