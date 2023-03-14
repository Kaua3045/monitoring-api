package com.kaua.monitoring.infrastructure.jobs.runnables;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.UUID;

@RequiredArgsConstructor
public class RunnableJob implements Runnable {

    private final JobLauncher jobLauncher;

    private final Job job;

    @Override
    public void run() {
        try {
            jobLauncher.run(job, new JobParametersBuilder()
                    .addString("id", UUID.randomUUID().toString())
                    .toJobParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
