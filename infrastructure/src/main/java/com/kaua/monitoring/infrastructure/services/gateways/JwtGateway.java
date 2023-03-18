package com.kaua.monitoring.infrastructure.services.gateways;

public interface JwtGateway {

    String extractTokenSubject(String token);
}
