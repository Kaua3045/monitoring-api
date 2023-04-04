package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import com.kaua.monitoring.application.usecases.checking.retrieve.ListLinkResponseByUrlIdCommand;
import com.kaua.monitoring.application.usecases.checking.retrieve.ListLinkResponseByUrlIdUseCase;
import com.kaua.monitoring.infrastructure.utils.InstantUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class LinkResponseService {

    private final ListLinkResponseByUrlIdUseCase listLinkResponseByUrlIdUseCase;

    public LinkResponseService(final ListLinkResponseByUrlIdUseCase listLinkResponseByUrlIdUseCase) {
        this.listLinkResponseByUrlIdUseCase = listLinkResponseByUrlIdUseCase;
    }

    public List<LinkResponseOutput> getFirst90LinkResponseByUrlId(
            final String urlId,
            final String startTimestamp,
            final String endTimestamp
    ) {

        final var startTimeVerified = startTimestamp.isBlank()
                ? Instant.now().minus(30, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS)
                : InstantUtils.parseToLocalTimeWithZonedInstant(startTimestamp);

        final var endTimeVerified = endTimestamp.isBlank()
                ? Instant.now().truncatedTo(ChronoUnit.SECONDS)
                : InstantUtils.parseToLocalTimeWithZonedInstant(endTimestamp);

        final var aCommand = new ListLinkResponseByUrlIdCommand(
                urlId,
                startTimeVerified,
                endTimeVerified
        );

        return this.listLinkResponseByUrlIdUseCase.execute(aCommand);
    }
}
