package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.link.create.CreateLinkCommand;
import com.kaua.monitoring.application.usecases.link.create.CreateLinkUseCase;
import com.kaua.monitoring.application.usecases.link.delete.DeleteLinkCommand;
import com.kaua.monitoring.application.usecases.link.delete.DeleteLinkUseCase;
import com.kaua.monitoring.application.usecases.link.outputs.CreateLinkOutput;
import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdCommand;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdUseCase;
import com.kaua.monitoring.infrastructure.link.inputs.CreateLinkBody;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class LinkService {

    private final CreateLinkUseCase createLinkUseCase;
    private final GetLinkByIdUseCase getLinkByIdUseCase;
    private final DeleteLinkUseCase deleteLinkUseCase;

    public LinkService(
            final CreateLinkUseCase createLinkUseCase,
            final GetLinkByIdUseCase getLinkByIdUseCase,
            final DeleteLinkUseCase deleteLinkUseCase
    ) {
        this.createLinkUseCase = createLinkUseCase;
        this.getLinkByIdUseCase = getLinkByIdUseCase;
        this.deleteLinkUseCase = deleteLinkUseCase;
    }

    public CreateLinkOutput createLink(CreateLinkBody body) {
        final var aLocalDateTime = LocalDateTime.parse(body.executeDate());
        final var aZonedDateTime = aLocalDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"));

        final var aCommand = new CreateLinkCommand(
                body.title(),
                body.url(),
                aZonedDateTime.toInstant(),
                body.linkExecution(),
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

    public void deleteLinkById(String id) {
        final var aCommand = new DeleteLinkCommand(id);
        this.deleteLinkUseCase.execute(aCommand);
    }
}
