package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.link.create.CreateLinkCommand;
import com.kaua.monitoring.application.usecases.link.create.CreateLinkUseCase;
import com.kaua.monitoring.application.usecases.link.outputs.CreateLinkOutput;
import com.kaua.monitoring.infrastructure.link.inputs.CreateLinkBody;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class LinkService {

    private final CreateLinkUseCase createLinkUseCase;

    public LinkService(final CreateLinkUseCase createLinkUseCase) {
        this.createLinkUseCase = createLinkUseCase;
    }

    public CreateLinkOutput createLink(CreateLinkBody body) {
        final var aCommand = new CreateLinkCommand(
                body.title(),
                body.url(),
                Instant.ofEpochMilli(body.executeDate()),
                body.repeat(),
                body.profileId()
        );

        final var aResult = this.createLinkUseCase.execute(aCommand);

        if (aResult.isLeft()) {
            throw aResult.getLeft();
        }

        return aResult.getRight();
    }
}
