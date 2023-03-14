package com.kaua.monitoring.infrastructure.jobs.links;

import com.kaua.monitoring.infrastructure.jobs.readers.EveryDayJobReader;
import com.kaua.monitoring.infrastructure.jobs.readers.NoRepeatJobReader;
import com.kaua.monitoring.infrastructure.jobs.readers.OnSpecificDayJobReader;
import com.kaua.monitoring.infrastructure.jobs.readers.TwoTimesAMonthJobReader;
import com.kaua.monitoring.infrastructure.link.persistence.LinkJpaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.UUID;

@Configuration
public class JobLinks {

    private final static Logger log = LoggerFactory.getLogger(JobLinks.class);

    private final static int VALUE_PER_CHUNK = 20;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public Job fetchUrlsJob(
            NoRepeatJobReader noRepeatJobReader,
            OnSpecificDayJobReader specificDayJobReader,
            TwoTimesAMonthJobReader twoTimesAMonthJobReader,
            EveryDayJobReader everyDayJobReader
    ) {
        return new JobBuilder("fetch-urls-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(noRepeatStep(noRepeatJobReader))
                .next(onSpecificDayStep(specificDayJobReader))
                .next(twoTimesAMonthStep(twoTimesAMonthJobReader))
                .next(everyDayStep(everyDayJobReader))
                .build();
    }

    @Bean
    public ItemWriter<LinkJpaEntity> writer() {
        return chunk -> log.info("{} on thread -> {}", chunk.getItems(), Thread.currentThread().getName());
    }

    @Bean
    @Qualifier("noRepeatStep")
    public Step noRepeatStep(
            NoRepeatJobReader noRepeatJobReader
    ) {
        return new StepBuilder("check-links-no-repeat-" + UUID.randomUUID(), jobRepository)
                .<LinkJpaEntity, LinkJpaEntity>chunk(VALUE_PER_CHUNK, transactionManager)
                .reader(noRepeatJobReader.noRepeatReader())
                .writer(writer())
                .build();
    }

    @Bean
    @Qualifier("onSpecificDayStep")
    public Step onSpecificDayStep(
            OnSpecificDayJobReader specificDayJobReader
    ) {
        return new StepBuilder("check-links-specific-day-" + UUID.randomUUID(), jobRepository)
                .<LinkJpaEntity, LinkJpaEntity>chunk(VALUE_PER_CHUNK, transactionManager)
                .reader(specificDayJobReader.specificDayReader())
                .writer(writer())
                .build();
    }

    @Bean
    @Qualifier("twoTimesAMonthStep")
    public Step twoTimesAMonthStep(
            TwoTimesAMonthJobReader twoTimesAMonthJobReader
    ) {
        return new StepBuilder("check-links-two-times-month-" + UUID.randomUUID(), jobRepository)
                .<LinkJpaEntity, LinkJpaEntity>chunk(VALUE_PER_CHUNK, transactionManager)
                .reader(twoTimesAMonthJobReader.twoTimesMonthReader())
                .writer(writer())
                .build();
    }

    @Bean
    @Qualifier("everyDayStep")
    public Step everyDayStep(
            EveryDayJobReader everyDayJobReader
    ) {
        return new StepBuilder("check-links-every-day-" + UUID.randomUUID(), jobRepository)
                .<LinkJpaEntity, LinkJpaEntity>chunk(VALUE_PER_CHUNK, transactionManager)
                .reader(everyDayJobReader.everyDayReader())
                .writer(writer())
                .build();
    }
}
