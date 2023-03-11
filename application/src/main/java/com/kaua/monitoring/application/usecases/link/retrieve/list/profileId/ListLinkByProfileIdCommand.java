package com.kaua.monitoring.application.usecases.link.retrieve.list.profileId;

import com.kaua.monitoring.domain.pagination.SearchQuery;

public record ListLinkByProfileIdCommand(String profileId, SearchQuery aQuery) {
}
