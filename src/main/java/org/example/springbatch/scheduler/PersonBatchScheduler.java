package org.example.springbatch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonBatchScheduler {

    private final Job job;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0/10 * * * * ?")
    public void runJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addJobParameter("Current Time", System.currentTimeMillis(), Long.class)
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
