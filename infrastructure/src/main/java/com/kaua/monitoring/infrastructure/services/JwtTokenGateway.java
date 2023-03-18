package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.infrastructure.services.gateways.JwtGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoders;

public class JwtTokenGateway implements JwtGateway {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Override
    public String extractTokenSubject(String token) {
        if (token.isBlank()) {
            return null;
        }

        final var decoderInstance = JwtDecoders.fromIssuerLocation(issuerUri);
        final var tokenDecoded = decoderInstance.decode(token.substring(7));

        return tokenDecoded.getSubject();
    }
}
