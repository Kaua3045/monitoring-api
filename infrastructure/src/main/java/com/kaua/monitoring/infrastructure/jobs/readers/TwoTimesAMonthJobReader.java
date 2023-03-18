package com.kaua.monitoring.infrastructure.jobs.readers;

import com.kaua.monitoring.infrastructure.jobs.readers.outputs.LinkResponseJob;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
public class TwoTimesAMonthJobReader {

    private static final String TWO_TIMES_A_MONTH_JOB_READER_NAME = "find-all-with-two-times-a-month";

    private static final String SQL_FIND_ALL_WITH_TWO_TIMES_A_MONTH = "SELECT id, url FROM links " +
            "WHERE (link_execution = 'TWO_TIMES_A_MONTH' AND CASE WHEN " +
            "date_part('day', current_timestamp - execute_date) >= 15 " +
            "THEN execute_date::time = current_timestamp::time AND " +
            "date_trunc('day', execute_date) = date_trunc('day', current_timestamp) " +
            "ELSE execute_date BETWEEN current_timestamp AND current_timestamp + '1 minutes'::interval END);";

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcCursorItemReader<LinkResponseJob> twoTimesMonthReader() {
        return new JdbcCursorItemReaderBuilder<LinkResponseJob>()
                .name(TWO_TIMES_A_MONTH_JOB_READER_NAME)
                .dataSource(dataSource)
                .sql(SQL_FIND_ALL_WITH_TWO_TIMES_A_MONTH)
                .rowMapper(new BeanPropertyRowMapper<>(LinkResponseJob.class))
                .build();
    }
}
