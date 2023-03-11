package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.link.create.CreateLinkCommand;
import com.kaua.monitoring.application.usecases.link.create.CreateLinkUseCase;
import com.kaua.monitoring.application.usecases.link.delete.DeleteLinkCommand;
import com.kaua.monitoring.application.usecases.link.delete.DeleteLinkUseCase;
import com.kaua.monitoring.application.usecases.link.outputs.CreateLinkOutput;
import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.application.usecases.link.outputs.UpdateLinkOutput;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdCommand;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdUseCase;
import com.kaua.monitoring.application.usecases.link.retrieve.list.profileId.ListLinkByProfileIdCommand;
import com.kaua.monitoring.application.usecases.link.retrieve.list.profileId.ListLinkByProfileIdUseCase;
import com.kaua.monitoring.application.usecases.link.update.UpdateLinkCommand;
import com.kaua.monitoring.application.usecases.link.update.UpdateLinkUseCase;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.domain.pagination.SearchQuery;
import com.kaua.monitoring.infrastructure.link.inputs.CreateLinkBody;
import com.kaua.monitoring.infrastructure.link.inputs.UpdateLinkBody;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class LinkService {

    private final CreateLinkUseCase createLinkUseCase;
    private final GetLinkByIdUseCase getLinkByIdUseCase;
    private final ListLinkByProfileIdUseCase listLinkByProfileIdUseCase;
    private final UpdateLinkUseCase updateLinkUseCase;
    private final DeleteLinkUseCase deleteLinkUseCase;

    public LinkService(
            final CreateLinkUseCase createLinkUseCase,
            final GetLinkByIdUseCase getLinkByIdUseCase,
            final ListLinkByProfileIdUseCase listLinkByProfileIdUseCase,
            final UpdateLinkUseCase updateLinkUseCase,
            final DeleteLinkUseCase deleteLinkUseCase
    ) {
        this.createLinkUseCase = createLinkUseCase;
        this.getLinkByIdUseCase = getLinkByIdUseCase;
        this.listLinkByProfileIdUseCase = listLinkByProfileIdUseCase;
        this.updateLinkUseCase = updateLinkUseCase;
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

    public Pagination<LinkOutput> getAllLinkByProfileId(
            String profileId,
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        final var aCommand = new ListLinkByProfileIdCommand(
                profileId,
                new SearchQuery(page, perPage, search, sort, direction)
        );
        return this.listLinkByProfileIdUseCase.execute(aCommand);
    }

    public UpdateLinkOutput updateLink(UpdateLinkBody body) {
        final var aLocalDateTime = LocalDateTime.parse(body.executeDate());
        final var aZonedDateTime = aLocalDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"));

        final var aCommand = new UpdateLinkCommand(
                body.id(),
                body.title(),
                body.url(),
                aZonedDateTime.toInstant(),
                body.linkExecution()
        );

        final var aResult = this.updateLinkUseCase.execute(aCommand);

        if (aResult.isLeft()) {
            throw aResult.getLeft();
        }

        return aResult.getRight();
    }

    public void deleteLinkById(String id) {
        final var aCommand = new DeleteLinkCommand(id);
        this.deleteLinkUseCase.execute(aCommand);
    }
}
