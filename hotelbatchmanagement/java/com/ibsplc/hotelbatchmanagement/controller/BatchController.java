package com.ibsplc.hotelbatchmanagement.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.Logger; 
import org.apache.logging.log4j.LogManager;  
import jakarta.validation.constraints.NotNull;  
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private static final Logger logger = LogManager.getLogger(BatchController.class);
    private static final String UPLOAD_DIR = "src/main/resources/uploads";
    private static final List<String> VALID_CSV_TYPES = Arrays.asList(
            "cities.csv", "hotels.csv", "pricing_details.csv",
            "pricing_plans.csv", "room_availability.csv", "rooms.csv");

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importInventoryJob;

    
    @Autowired
    private JobRepository jobRepository;
    

    @Autowired
    private Step uploadedCityStep;

    @Autowired
    private Step uploadedHotelStep;

    @Autowired
    private Step uploadedPricingDetailsStep;

    @Autowired
    private Step uploadedRoomAvailabilityStep;

    @Autowired
    private Step uploadedPricingPlanStep;

    @Autowired
    private Step uploadedRoomsStep;
    
    @PostMapping("/run")
    public ResponseEntity<String> runBatchJob() {
        try {
            logger.info("Initiating batch job execution");
            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(importInventoryJob, params);
            logger.info("Batch job started successfully");
            return ResponseEntity.ok("Inventory batch job started successfully");
        } catch (Exception e) {
            logger.error("Failed to start batch job", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error starting inventory batch job: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> updateBatchData(
            @RequestParam("file") @NotNull MultipartFile file) {
        Map<String, String> response = new HashMap<>();

        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || !VALID_CSV_TYPES.contains(fileName.toLowerCase())) {
                response.put("error", "Invalid file type. Please upload a valid CSV file: " + VALID_CSV_TYPES);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (file.isEmpty()) {
                response.put("error", "Uploaded file is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Path uploadPath = Path.of(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filePath = UPLOAD_DIR + "/" + fileName;
            Files.copy(file.getInputStream(), Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);

            logger.info("Updated file: {}", fileName);

            Step selectedStep;
            switch (fileName.toLowerCase()) {
                case "cities.csv":
                    selectedStep = uploadedCityStep;
                    break;
                case "hotels.csv":
                    selectedStep = uploadedHotelStep;
                    break;
                case "pricing_details.csv":
                    selectedStep = uploadedPricingDetailsStep;
                    break;
                case "room_availability.csv":
                    selectedStep = uploadedRoomAvailabilityStep;
                    break;
                case "pricing_plans.csv":
                    selectedStep = uploadedPricingPlanStep;
                    break;
                case "rooms.csv":
                    selectedStep = uploadedRoomsStep;
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported file: " + fileName);
            }

            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .addString("fileName", fileName)
                    .addString("filePath", filePath)
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            // Use injected jobRepository instead
            Job dynamicJob = new JobBuilder("importUploadedInventoryJob", jobRepository)
                    .start(selectedStep)
                    .build();

            jobLauncher.run(dynamicJob, params);

            response.put("message", "Successfully updated " + fileName);
            response.put("status", "processing");

            logger.info("Update process started for file: {}", fileName);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error processing update for file: {}", file.getOriginalFilename(), e);
            response.put("error", "Failed to process update: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Batch Controller is operational");
    }
}