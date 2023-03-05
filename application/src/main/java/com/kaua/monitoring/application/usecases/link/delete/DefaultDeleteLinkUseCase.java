package com.kaua.monitoring.application.usecases.link.delete;

import com.kaua.monitoring.application.gateways.LinkGateway;

public class DefaultDeleteLinkUseCase extends DeleteLinkUseCase {

    private final LinkGateway linkGateway;

    public DefaultDeleteLinkUseCase(final LinkGateway linkGateway) {
        this.linkGateway = linkGateway;
    }

    @Override
    public void execute(DeleteLinkCommand aCommand) {
        this.linkGateway.deleteById(aCommand.id());
    }
}
