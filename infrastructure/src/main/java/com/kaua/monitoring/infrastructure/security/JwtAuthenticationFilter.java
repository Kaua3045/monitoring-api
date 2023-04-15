package com.kaua.monitoring.infrastructure.security;

import com.kaua.monitoring.application.gateways.JwtGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtGateway jwtGateway;
    private final ProfileGateway profileGateway;

    public JwtAuthenticationFilter(
            final JwtGateway jwtGateway,
            final ProfileGateway profileGateway
    ) {
        this.jwtGateway = jwtGateway;
        this.profileGateway = profileGateway;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        final var authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final var jwtToken = authHeader.substring(7);

        try {
            final var profileId = this.jwtGateway.extractTokenSubject(jwtToken);

            if (profileId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final var aProfile = this.profileGateway.findById(profileId);

                if (aProfile.isEmpty()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                if (this.jwtGateway.isTokenValid(jwtToken, aProfile.get().getId().getValue())) {
                    final var aProfileAuthenticated = new UsernamePasswordAuthenticationToken(
                            aProfile.get(),
                            null,
                            new ArrayList<>()
                    );

                    aProfileAuthenticated.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(aProfileAuthenticated);
                }
            }

            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            filterChain.doFilter(request, response);
        }
    }
}
