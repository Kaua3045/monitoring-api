package com.kaua.monitoring.infrastructure.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class KeycloakAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        final Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get(REALM_ACCESS);

        return ((List<String>) realmAccess.get(ROLES)).stream()
                .map(roleName -> {
                    return "ROLE_" + roleName;
                }) // prefix to map to a Spring Security "role"
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
