package com.kaua.monitoring.infrastructure.jobs.configuration;

import com.kaua.monitoring.infrastructure.jobs.links.JobLinks;
import com.kaua.monitoring.infrastructure.jobs.readers.EveryDayJobReader;
import com.kaua.monitoring.infrastructure.jobs.readers.NoRepeatJobReader;
import com.kaua.monitoring.infrastructure.jobs.readers.OnSpecificDayJobReader;
import com.kaua.monitoring.infrastructure.jobs.readers.TwoTimesAMonthJobReader;
import com.kaua.monitoring.infrastructure.jobs.runnables.RunnableJob;
import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class StartJobs {

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobLinks jobLinks;

    @Autowired
    private NoRepeatJobReader noRepeatJobReader;

    @Autowired
    private OnSpecificDayJobReader specificDayJobReader;

    @Autowired
    private TwoTimesAMonthJobReader twoTimesAMonthJobReader;

    @Autowired
    private EveryDayJobReader everyDayJobReader;

//    @PostConstruct
    public void fetchUrlJobExecution() {
        taskScheduler.scheduleWithFixedDelay(
                new RunnableJob(
                        jobLauncher,
                        jobLinks.fetchUrlsJob(
                                noRepeatJobReader,
                                specificDayJobReader,
                                twoTimesAMonthJobReader,
                                everyDayJobReader
                        )),
                Duration.ofMinutes(1));
    }
}
