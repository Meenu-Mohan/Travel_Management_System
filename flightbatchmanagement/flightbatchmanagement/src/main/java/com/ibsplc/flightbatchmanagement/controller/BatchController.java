package com.ibsplc.flightbatchmanagement.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importFlightJob;

    @PostMapping("/run")
    public ResponseEntity<String> runBatchJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis())) // Unique parameter for each run
                    .toJobParameters();
            jobLauncher.run(importFlightJob, params);
            return ResponseEntity.ok("Batch job started successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error starting batch job: " + e.getMessage());
        }
    }
}