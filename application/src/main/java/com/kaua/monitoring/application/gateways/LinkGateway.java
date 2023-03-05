package com.kaua.monitoring.application.gateways;

import com.kaua.monitoring.domain.links.Link;

import java.util.List;
import java.util.Optional;

public interface LinkGateway {

    Link create(final Link aLink);

    Optional<Link> findById(final String id);

    List<Link> findAllByProfileId(final String profileId);

    Link update(final Link aLink);

    void deleteById(final String id);
}
