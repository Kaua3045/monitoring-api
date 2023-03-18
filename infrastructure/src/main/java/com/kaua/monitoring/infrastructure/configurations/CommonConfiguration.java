package com.kaua.monitoring.infrastructure.configurations;

import com.kaua.monitoring.infrastructure.services.JwtTokenService;
import com.kaua.monitoring.infrastructure.services.gateways.JwtGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    @Bean
    public JwtGateway jwtGateway() {
        return new JwtTokenService();
    }
}
