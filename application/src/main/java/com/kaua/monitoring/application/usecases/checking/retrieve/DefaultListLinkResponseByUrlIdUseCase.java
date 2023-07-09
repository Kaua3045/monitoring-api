package com.kaua.monitoring.application.usecases.checking.retrieve;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.gateways.LinkGateway;
import com.kaua.monitoring.application.gateways.LinkResponseGateway;
import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import com.kaua.monitoring.domain.links.Link;

import java.util.List;

public class DefaultListLinkResponseByUrlIdUseCase extends ListLinkResponseByUrlIdUseCase {

    private final LinkResponseGateway linkResponseGateway;
    private final LinkGateway linkGateway;

    public DefaultListLinkResponseByUrlIdUseCase(
            final LinkResponseGateway linkResponseGateway,
            final LinkGateway linkGateway
    ) {
        this.linkResponseGateway = linkResponseGateway;
        this.linkGateway = linkGateway;
    }

    @Override
    public List<LinkResponseOutput> execute(ListLinkResponseByUrlIdCommand aCommand) {
        final var linkExists = this.linkGateway.findById(aCommand.urlId())
                .orElseThrow(() -> new NotFoundException(Link.class, aCommand.urlId()));

        return this.linkResponseGateway.findAllByUrlIdAndFilterByVerifiedDate(
                linkExists.getId().getValue(),
                aCommand.startTimestamp(),
                aCommand.endTimestamp()
        ).stream().map(LinkResponseOutput::from).toList();
    }
}
