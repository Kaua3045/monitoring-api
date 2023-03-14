package com.kaua.monitoring.infrastructure.jobs.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ThreadPoolTaskConfiguration {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        final var threadPoolTask = new ThreadPoolTaskScheduler();
        threadPoolTask.setPoolSize(4);
        threadPoolTask.setThreadNamePrefix("Job-Scheduler");
        threadPoolTask.initialize();
        return threadPoolTask;
    }
}
