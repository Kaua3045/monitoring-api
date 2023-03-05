package com.kaua.monitoring.application.usecases.link.retrieve.list.profileId;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.domain.profile.Profile;

import java.util.List;

public class DefaultListLinkByProfileIdUseCase extends ListLinkByProfileIdUseCase {

    private final LinkGateway linkGateway;
    private final ProfileGateway profileGateway;

    public DefaultListLinkByProfileIdUseCase(final LinkGateway linkGateway, final ProfileGateway profileGateway) {
        this.linkGateway = linkGateway;
        this.profileGateway = profileGateway;
    }

    @Override
    public List<LinkOutput> execute(ListLinkByProfileIdCommand aCommand) {
        final var profileExists = this.profileGateway.findById(aCommand.profileId())
                .orElseThrow(() -> new NotFoundException(Profile.class, aCommand.profileId()));

        return this.linkGateway.findAllByProfileId(profileExists.getId().getValue())
                .stream().map(LinkOutput::from)
                .toList();
    }
}
