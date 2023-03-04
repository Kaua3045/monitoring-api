package com.kaua.monitoring.infrastructure.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final KeycloakAuthoritiesConverter authoritiesConverter;

    public KeycloakConverter() {
        this.authoritiesConverter = new KeycloakAuthoritiesConverter();
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        return new JwtAuthenticationToken(source, extractAuthorities(source), extractPrincipal(source));
    }

    private String extractPrincipal(final Jwt jwt) {
        return jwt.getClaimAsString(JwtClaimNames.SUB);
    }

    private Collection<? extends GrantedAuthority> extractAuthorities(final Jwt jwt) {
        return this.authoritiesConverter.convert(jwt);
    }
}
