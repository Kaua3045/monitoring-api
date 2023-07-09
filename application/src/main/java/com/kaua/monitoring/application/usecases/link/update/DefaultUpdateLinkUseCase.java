package com.kaua.monitoring.application.usecases.link.update;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.usecases.link.outputs.UpdateLinkOutput;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;

public class DefaultUpdateLinkUseCase extends UpdateLinkUseCase {

    private final LinkGateway linkGateway;

    public DefaultUpdateLinkUseCase(final LinkGateway linkGateway) {
        this.linkGateway = linkGateway;
    }

    @Override
    public Either<DomainException, UpdateLinkOutput> execute(UpdateLinkCommand aCommand) {
        final var linkExists = this.linkGateway.findById(aCommand.id())
                .orElseThrow(() -> new NotFoundException(Link.class, aCommand.id()));

        final var aExecuteDate = aCommand.executeDate() == null
                ? linkExists.getExecuteDate()
                : aCommand.executeDate();

        final var aTitle = aCommand.title() == null
                ? linkExists.getTitle()
                : aCommand.title();

        final var aUrl = aCommand.url() == null
                ? linkExists.getUrl()
                : aCommand.url();

        final var aLinkExecution = aCommand.linkExecution() == null
                ? linkExists.getLinkExecution()
                : LinkExecutions.valueOf(aCommand.linkExecution());

        final var aNextExecuteDate = aCommand.executeDate();

        final var aLinkUpdated = linkExists.update(
                aTitle,
                aUrl,
                aExecuteDate,
                aNextExecuteDate,
                aLinkExecution
        );

        final var aLinkValidated = aLinkUpdated.validate();

        if (!aLinkValidated.isEmpty()) {
            return Either.left(DomainException.with(aLinkValidated));
        }

        return Either.right(new UpdateLinkOutput(this.linkGateway.update(aLinkUpdated).getId().getValue()));
    }
}
