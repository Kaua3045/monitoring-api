package com.kaua.monitoring.application.usecases.link.create;

import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.link.outputs.CreateLinkOutput;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.profile.Profile;

public class DefaultCreateLinkUseCase extends CreateLinkUseCase {

    private final LinkGateway linkGateway;
    private final ProfileGateway profileGateway;

    public DefaultCreateLinkUseCase(
            final LinkGateway linkGateway,
            final ProfileGateway profileGateway
    ) {
        this.linkGateway = linkGateway;
        this.profileGateway = profileGateway;
    }

    @Override
    public Either<DomainException, CreateLinkOutput> execute(CreateLinkCommand aCommand) {
        final var profileExists = this.profileGateway.findById(aCommand.profileId())
                .orElseThrow(() -> new NotFoundException(Profile.class, aCommand.profileId()));

        final var aLink = Link.newLink(
                aCommand.title(),
                aCommand.url(),
                aCommand.executeDate(),
                aCommand.repeat(),
                profileExists
        );

        final var aLinkValidated = aLink.validate();

        if (!aLinkValidated.isEmpty()) {
            return Either.left(DomainException.with(aLinkValidated));
        }

        return Either.right(new CreateLinkOutput(this.linkGateway.create(aLink).getId().getValue()));
    }
}
