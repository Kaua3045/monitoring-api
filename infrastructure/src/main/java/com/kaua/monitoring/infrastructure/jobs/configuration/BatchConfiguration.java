package com.kaua.monitoring.infrastructure.jobs.configuration;

import com.kaua.monitoring.infrastructure.jobs.listener.JobCompletionNotificationListener;
import com.kaua.monitoring.infrastructure.jobs.outputs.LinkJobOutput;
import com.kaua.monitoring.infrastructure.jobs.processor.UrlItemProcessor;
import com.kaua.monitoring.infrastructure.jobs.reader.LinkJobReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.Instant;

@Configuration
@EnableScheduling
public class BatchConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);

    private static final String QUERY_FIND_ALL =
            "select * from links where execute_date between current_timestamp and current_timestamp + '1 minutes'::interval";
//    minutes or seconds (cron vai executar todo minuto, então o certo seria pegar o momento atual)
//    banco esta salvando em UTC;

//    SELECT * FROM links WHERE execute_date::date = current_date
//    select * from links where execute_date > date_trunc('hour', NOW()::TIMESTAMP);
//    select * from links where execute_date between current_timestamp and current_timestamp + '3 minutes'::interval
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JobCompletionNotificationListener listener;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobLauncher jobLauncher;

//    executa meu job de tempo em tempo
//    @Scheduled(cron = "*/1 * * * * *")
    @Scheduled(fixedRate = 60000)
    public void execute() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobExecution execution = jobLauncher.run(
                new JobBuilder("teste", jobRepository)
                        .incrementer(new RunIdIncrementer())
                        .listener(listener)
                        .flow(step1())
                        .end()
                        .build(),
                new JobParametersBuilder()
                        .addString("time", Instant.now().toString())
                        .toJobParameters());

        log.info("Job {} status -> {}", execution.getJobId(), execution.getStatus());
    }

    @Bean
    public JdbcCursorItemReader<LinkJobReader> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<LinkJobReader>()
                .name("links-find-all")
                .dataSource(dataSource)
                .sql(QUERY_FIND_ALL)
                .rowMapper(new BeanPropertyRowMapper<>(LinkJobReader.class))
                .build();
    }

    @Bean
    public UrlItemProcessor processor() {
        return new UrlItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<LinkJobOutput> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<LinkJobOutput>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO links_responses (id, urlId, response_message, status_code) " +
                        "VALUES (:id, :urlId, :responseMessage, :statusCode)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step step1(
    ) {
        return new StepBuilder("check-links", jobRepository)
                .<LinkJobReader, LinkJobOutput>chunk(10, transactionManager)
                .reader(reader(dataSource))
                .processor(processor())
                .writer(writer(dataSource))
                .build();
    }
}