package org.example.springbatch.batch;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobCompletionListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! Job Completed! Time to verify the results");
        } else {
            log.warn("Job NOT Completed... Status: {}", jobExecution.getStatus());
        }

        try {
            jdbcTemplate
                    .query("SELECT first_name, last_namee FROM people", new DataClassRowMapper<>(Person.class))
                    .forEach(person ->
                            log.info("Found <{{}}> in the database.", person)
                    );

            jdbcTemplate.execute("DELETE FROM people");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
