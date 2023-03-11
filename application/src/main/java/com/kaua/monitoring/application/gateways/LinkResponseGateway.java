package com.kaua.monitoring.application.gateways;

import com.kaua.monitoring.domain.checking.LinkResponse;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.domain.pagination.SearchQuery;

public interface LinkResponseGateway {

    Pagination<LinkResponse> findAllByUrlId(final String urlId, final SearchQuery aQuery);
}
