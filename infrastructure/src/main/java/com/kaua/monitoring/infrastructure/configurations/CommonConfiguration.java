package com.kaua.monitoring.infrastructure.configurations;

import com.kaua.monitoring.application.gateways.EncrypterGateway;
import com.kaua.monitoring.application.gateways.JwtGateway;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.infrastructure.security.JwtAuthenticationFilter;
import com.kaua.monitoring.infrastructure.services.BcryptService;
import com.kaua.monitoring.infrastructure.services.JwtTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class CommonConfiguration {

    @Value("${spring.jwt.secret}")
    private String SECRET;

    @Value("${spring.jwt.expire}")
    private String EXPIRATION;

    private final ProfileGateway profileGateway;

    public CommonConfiguration(final ProfileGateway profileGateway) {
        this.profileGateway = profileGateway;
    }

    @Bean
    public JwtGateway jwtGatewayImp() {
        return new JwtTokenService(SECRET, EXPIRATION);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtGatewayImp(), profileGateway);
    }

    @Bean
    public EncrypterGateway encrypterGateway(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new BcryptService(bCryptPasswordEncoder);
    }
}
