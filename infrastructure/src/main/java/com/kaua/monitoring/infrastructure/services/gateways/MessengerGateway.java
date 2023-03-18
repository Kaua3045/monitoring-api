package com.kaua.monitoring.infrastructure.services.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MessengerGateway {

    void sendMessage(Object message) throws JsonProcessingException;
}
