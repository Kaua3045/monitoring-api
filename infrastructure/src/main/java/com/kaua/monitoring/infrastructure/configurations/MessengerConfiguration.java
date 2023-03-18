package com.kaua.monitoring.infrastructure.configurations;

import com.kaua.monitoring.infrastructure.services.SQSService;
import com.kaua.monitoring.infrastructure.services.gateways.MessengerGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessengerConfiguration {

    @Bean
    public MessengerGateway messengerGateway() {
        return new SQSService();
    }
}
