package com.kaua.monitoring.application.gateways;

import com.kaua.monitoring.domain.checking.LinkResponse;

import java.util.List;

public interface LinkResponseGateway {

    List<LinkResponse> findAllTop90(final String urlId);
}
