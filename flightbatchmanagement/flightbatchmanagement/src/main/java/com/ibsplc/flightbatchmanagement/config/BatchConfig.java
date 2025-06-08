package com.ibsplc.flightbatchmanagement.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.ibsplc.flightbatchmanagement.entity.Flight;
import com.ibsplc.flightbatchmanagement.processor.FlightItemProcessor;
import com.ibsplc.flightbatchmanagement.reader.FlightItemReader;
import com.ibsplc.flightbatchmanagement.writer.FlightItemWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private FlightItemReader flightItemReader;

    @Autowired
    private FlightItemProcessor flightItemProcessor;

    @Autowired
    private FlightItemWriter flightItemWriter;

    @Bean
    public Step flightStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("flightStep", jobRepository)
                .<Flight, Flight>chunk(100, transactionManager) // Process 100 items at a time
                .reader(flightItemReader.flightReader())
                .processor(flightItemProcessor)
                .writer(flightItemWriter)
                .build();
    }

    @Bean
    public Job importFlightJob(JobRepository jobRepository, Step flightStep) {
        return new JobBuilder("importFlightJob", jobRepository)
                .start(flightStep)
                .build();
    }
}