package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.link.create.CreateLinkCommand;
import com.kaua.monitoring.application.usecases.link.create.CreateLinkUseCase;
import com.kaua.monitoring.application.usecases.link.outputs.CreateLinkOutput;
import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdCommand;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdUseCase;
import com.kaua.monitoring.application.usecases.link.retrieve.list.profileId.ListLinkByProfileIdCommand;
import com.kaua.monitoring.application.usecases.link.retrieve.list.profileId.ListLinkByProfileIdUseCase;
import com.kaua.monitoring.infrastructure.link.inputs.CreateLinkBody;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LinkService {

    private final CreateLinkUseCase createLinkUseCase;
    private final GetLinkByIdUseCase getLinkByIdUseCase;
    private final ListLinkByProfileIdUseCase listLinkByProfileIdUseCase;

    public LinkService(
            final CreateLinkUseCase createLinkUseCase,
            final GetLinkByIdUseCase getLinkByIdUseCase,
            final ListLinkByProfileIdUseCase listLinkByProfileIdUseCase
    ) {
        this.createLinkUseCase = createLinkUseCase;
        this.getLinkByIdUseCase = getLinkByIdUseCase;
        this.listLinkByProfileIdUseCase = listLinkByProfileIdUseCase;
    }

    public CreateLinkOutput createLink(CreateLinkBody body) {
        final var executeDate = LocalDateTime.parse(body.executeDate());

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

    public List<LinkOutput> getAllLinksByProfileId(String profileId) {
        final var aCommand = new ListLinkByProfileIdCommand(profileId);
        return this.listLinkByProfileIdUseCase.execute(aCommand);
    }
}
