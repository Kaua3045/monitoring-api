package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import com.kaua.monitoring.application.usecases.checking.retrieve.ListLinkResponseByUrlIdCommand;
import com.kaua.monitoring.application.usecases.checking.retrieve.ListLinkResponseByUrlIdUseCase;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LinkResponseService {

    private final ListLinkResponseByUrlIdUseCase listLinkResponseByUrlIdUseCase;

    public LinkResponseService(final ListLinkResponseByUrlIdUseCase listLinkResponseByUrlIdUseCase) {
        this.listLinkResponseByUrlIdUseCase = listLinkResponseByUrlIdUseCase;
    }

    public List<LinkResponseOutput> getFirst90LinkResponseByUrlId(String urlId) {
        final var aCommand = new ListLinkResponseByUrlIdCommand(urlId);
        return this.listLinkResponseByUrlIdUseCase.execute(aCommand);
    }
}
