package com.ibsplc.hotelbatchmanagement.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.ibsplc.hotelbatchmanagement.entity.City;
import com.ibsplc.hotelbatchmanagement.entity.Hotel;
import com.ibsplc.hotelbatchmanagement.entity.PricingDetails;
import com.ibsplc.hotelbatchmanagement.entity.PricingPlan;
import com.ibsplc.hotelbatchmanagement.entity.RoomAvailability;
import com.ibsplc.hotelbatchmanagement.entity.Rooms;
import com.ibsplc.hotelbatchmanagement.processor.CityItemProcessor;
import com.ibsplc.hotelbatchmanagement.processor.HotelItemProcessor;
import com.ibsplc.hotelbatchmanagement.processor.PricingDetailsItemProcessor;
import com.ibsplc.hotelbatchmanagement.processor.PricingPlanItemProcessor;
import com.ibsplc.hotelbatchmanagement.processor.RoomAvailabilityProcessor;
import com.ibsplc.hotelbatchmanagement.processor.RoomsItemProcessor;
import com.ibsplc.hotelbatchmanagement.reader.CityItemReader;
import com.ibsplc.hotelbatchmanagement.reader.HotelItemReader;
import com.ibsplc.hotelbatchmanagement.reader.PricingDetailsItemReader;
import com.ibsplc.hotelbatchmanagement.reader.PricingPlanItemReader;
import com.ibsplc.hotelbatchmanagement.reader.RoomAvailabilityItemReader;
import com.ibsplc.hotelbatchmanagement.reader.RoomsItemReader;
import com.ibsplc.hotelbatchmanagement.writer.CityItemWriter;
import com.ibsplc.hotelbatchmanagement.writer.HotelItemWriter;
import com.ibsplc.hotelbatchmanagement.writer.PricingDetailsItemWriter;
import com.ibsplc.hotelbatchmanagement.writer.PricingPlanItemWriter;
import com.ibsplc.hotelbatchmanagement.writer.RoomAvailabilityItemWriter;
import com.ibsplc.hotelbatchmanagement.writer.RoomsItemWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private static final int CHUNK_SIZE = 100;

    @Autowired
    private CityItemReader cityItemReader;

    @Autowired
    private CityItemProcessor cityItemProcessor;

    @Autowired
    private CityItemWriter cityItemWriter;

    @Autowired
    private HotelItemReader hotelItemReader;

    @Autowired
    private HotelItemProcessor hotelItemProcessor;

    @Autowired
    private HotelItemWriter hotelItemWriter;

    @Autowired
    private RoomAvailabilityItemReader roomAvailabilityItemReader;

    @Autowired
    private RoomAvailabilityProcessor roomAvailabilityItemProcessor;

    @Autowired
    private RoomAvailabilityItemWriter roomAvailabilityItemWriter;

    @Autowired
    private PricingDetailsItemReader pricingDetailsItemReader;

    @Autowired
    private PricingDetailsItemProcessor pricingDetailsItemProcessor;

    @Autowired
    private PricingDetailsItemWriter pricingDetailsItemWriter;

    @Autowired
    private PricingPlanItemReader pricingPlanItemReader;

    @Autowired
    private PricingPlanItemProcessor pricingPlanItemProcessor;

    @Autowired
    private PricingPlanItemWriter pricingPlanItemWriter;

    @Autowired
    private RoomsItemReader roomsItemReader;

    @Autowired
    private RoomsItemProcessor roomsItemProcessor;

    @Autowired
    private RoomsItemWriter roomsItemWriter;

    /**
     * Generic method to build a step with a given name, reader, processor, writer,
     * and chunk size.
     */
    private <T> Step buildStep(String stepName, JobRepository jobRepository,
            PlatformTransactionManager transactionManager, ItemReader<T> reader, ItemProcessor<T, T> processor,
            ItemWriter<T> writer) {
        return new StepBuilder(stepName, jobRepository).<T, T>chunk(CHUNK_SIZE, transactionManager).reader(reader)
                .processor(processor).writer(writer).build();
    }

    @Bean
    public Step cityStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return buildStep("cityStep", jobRepository, transactionManager, cityItemReader.cityReader(), cityItemProcessor,
                cityItemWriter);
    }

    @Bean
    public Step roomsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return buildStep("roomsStep", jobRepository, transactionManager, roomsItemReader.roomsReader(),
                roomsItemProcessor, roomsItemWriter);
    }

    @Bean
    public Step pricingDetailsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return buildStep("pricingDetailsStep", jobRepository, transactionManager,
                pricingDetailsItemReader.pricingDetailsReader(), pricingDetailsItemProcessor, pricingDetailsItemWriter);
    }

    @Bean
    public Step roomAvailabilityStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return buildStep("roomAvailabilityStep", jobRepository, transactionManager,
                roomAvailabilityItemReader.roomAvailabilityReader(), roomAvailabilityItemProcessor,
                roomAvailabilityItemWriter);
    }

    @Bean
    public Step hotelDetailsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return buildStep("hotelDetailsStep", jobRepository, transactionManager, hotelItemReader.hotelReader(),
                hotelItemProcessor, hotelItemWriter);
    }

    @Bean
    public Step pricingPlanStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return buildStep("pricingPlanStep", jobRepository, transactionManager,
                pricingPlanItemReader.pricingPlanReader(), pricingPlanItemProcessor, pricingPlanItemWriter);
    }

    @Bean
    public Job importInventoryJob(JobRepository jobRepository, Step cityStep, Step pricingDetailsStep,
            Step roomAvailabilityStep, Step roomsStep, Step hotelDetailsStep, Step pricingPlanStep) {
        return new JobBuilder("importInventoryJob", jobRepository).start(cityStep).next(pricingDetailsStep)
                .next(roomAvailabilityStep).next(roomsStep).next(hotelDetailsStep).next(pricingPlanStep).build();
    }

    // New Steps for Uploaded Files (using existing readers, processors, writers)
    @Bean
    @JobScope // Allows JobParameters to be injected
    public Step uploadedCityStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<City> reader = cityItemReader.cityReader();
        if (filePath != null) {
            reader.setResource(new FileSystemResource(filePath)); // Override resource at runtime
        }
        return buildStep("uploadedCityStep", jobRepository, transactionManager, reader, cityItemProcessor, cityItemWriter);
    }

    @Bean
    @JobScope
    public Step uploadedHotelStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<Hotel> reader = hotelItemReader.hotelReader();
        if (filePath != null) {
            reader.setResource(new FileSystemResource(filePath));
        }
        return buildStep("uploadedHotelStep", jobRepository, transactionManager, reader, hotelItemProcessor, hotelItemWriter);
    }

    @Bean
    @JobScope
    public Step uploadedPricingDetailsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<PricingDetails> reader = pricingDetailsItemReader.pricingDetailsReader();
        if (filePath != null) {
            reader.setResource(new FileSystemResource(filePath));
        }
        return buildStep("uploadedPricingDetailsStep", jobRepository, transactionManager, reader, pricingDetailsItemProcessor, pricingDetailsItemWriter);
    }

    @Bean
    @JobScope
    public Step uploadedRoomAvailabilityStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<RoomAvailability> reader = roomAvailabilityItemReader.roomAvailabilityReader();
        if (filePath != null) {
            reader.setResource(new FileSystemResource(filePath));
        }
        return buildStep("uploadedRoomAvailabilityStep", jobRepository, transactionManager, reader, roomAvailabilityItemProcessor, roomAvailabilityItemWriter);
    }

    @Bean
    @JobScope
    public Step uploadedPricingPlanStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<PricingPlan> reader = pricingPlanItemReader.pricingPlanReader();
        if (filePath != null) {
            reader.setResource(new FileSystemResource(filePath));
        }
        return buildStep("uploadedPricingPlanStep", jobRepository, transactionManager, reader, pricingPlanItemProcessor, pricingPlanItemWriter);
    }

    @Bean
    @JobScope
    public Step uploadedRoomsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<Rooms> reader = roomsItemReader.roomsReader();
        if (filePath != null) {
            reader.setResource(new FileSystemResource(filePath));
        }
        return buildStep("uploadedRoomsStep", jobRepository, transactionManager, reader, roomsItemProcessor, roomsItemWriter);
    }

    @Bean
    public Job importUploadedInventoryJob(JobRepository jobRepository,
            Step uploadedCityStep, 
            Step uploadedHotelStep, 
            Step uploadedPricingDetailsStep,
            Step uploadedRoomAvailabilityStep, 
            Step uploadedPricingPlanStep, 
            Step uploadedRoomsStep) {
        return new JobBuilder("importUploadedInventoryJob", jobRepository)
                .start(uploadedCityStep) // Default step, will be overridden by controller logic
                .build();
    }
}