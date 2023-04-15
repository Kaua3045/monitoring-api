package com.kaua.monitoring.infrastructure.services;

import com.kaua.monitoring.application.gateways.JwtGateway;
import com.kaua.monitoring.infrastructure.utils.InstantUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtTokenService implements JwtGateway {

    private final String JWT_SECRET_KEY;
    private final String JWT_EXPIRATION_TIME;

    public JwtTokenService(
            final String secret,
            final String expire
    ) {
        this.JWT_SECRET_KEY = secret;
        this.JWT_EXPIRATION_TIME = expire;
    }

    @Override
    public String extractTokenSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public String generateToken(String subject) {
        return Jwts
                .builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(InstantUtils.now()))
                .setExpiration(Date.from(InstantUtils.now().plus(
                        Integer.parseInt(JWT_EXPIRATION_TIME),
                        ChronoUnit.DAYS)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, String subject) {
        final var profileId = extractTokenSubject(token);
        return profileId.equals(subject) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims();
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
