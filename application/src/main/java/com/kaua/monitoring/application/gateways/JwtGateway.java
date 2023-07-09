package com.kaua.monitoring.application.gateways;

public interface JwtGateway {

    String extractTokenSubject(String token);

    String generateToken(String subject);

    boolean isTokenValid(String token, String subject);

    boolean isTokenExpired(String token);
}
