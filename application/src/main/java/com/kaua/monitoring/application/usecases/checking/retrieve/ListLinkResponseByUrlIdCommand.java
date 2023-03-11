package com.kaua.monitoring.application.usecases.checking.retrieve;

import com.kaua.monitoring.domain.pagination.SearchQuery;

public record ListLinkResponseByUrlIdCommand(String urlId, SearchQuery aQuery) {
}
