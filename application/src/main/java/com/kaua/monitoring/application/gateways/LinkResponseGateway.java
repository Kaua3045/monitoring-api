package com.kaua.monitoring.application.gateways;

import com.kaua.monitoring.domain.checking.LinkResponse;

import java.time.Instant;
import java.util.List;

public interface LinkResponseGateway {

    List<LinkResponse> findAllByUrlIdAndFilterByVerifiedDate(String urlId, Instant startTimestamp, Instant endTimestamp);
}
