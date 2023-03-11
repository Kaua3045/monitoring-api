package com.kaua.monitoring.application.usecases.link.retrieve.get;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.domain.links.Link;

public class DefaultGetLinkByIdUseCase extends GetLinkByIdUseCase {

    private final LinkGateway linkGateway;

    public DefaultGetLinkByIdUseCase(final LinkGateway linkGateway) {
        this.linkGateway = linkGateway;
    }

    @Override
    public LinkOutput execute(GetLinkByIdCommand aCommand) {
        return this.linkGateway.findById(aCommand.id())
                .map(LinkOutput::from)
                .orElseThrow(() -> new NotFoundException(Link.class, aCommand.id()));
    }
}
