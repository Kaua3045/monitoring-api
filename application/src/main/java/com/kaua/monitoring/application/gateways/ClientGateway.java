package com.kaua.monitoring.application.gateways;

import com.kaua.monitoring.domain.client.Client;

import java.util.Optional;

public interface ClientGateway {

    Client create(Client aClient);

    Optional<Client> findById(String id);

    Optional<Client> findByEmail(String email);

    Client update(Client aClient);

    void deleteById(String id);
}
