package com.kaua.monitoring.application.gateways;

import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.domain.pagination.SearchQuery;

import java.util.Optional;

public interface LinkGateway {

    Link create(final Link aLink);

    Optional<Link> findById(final String id);

    Pagination<Link> findAllByProfileId(final String profileId, final SearchQuery aQuery);

    Link update(final Link aLink);

    void deleteById(final String id);
}
