package hotelavailability.controller;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hotelavailability.dto.HotelAndRoomsDTO;
import hotelavailability.service.HotelSearchService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
 
@RestController
@RequestMapping("/hotelAvailability")
public class HotelSearchController {
 
    private static final Logger logger = LogManager.getLogger(HotelSearchController.class);
 
    @Autowired
    private HotelSearchService hotelSearchService;
 
    @GetMapping("/hotelSearch")
    public List<HotelAndRoomsDTO> searchHotels(@RequestParam String cityName, @RequestParam String checkInDate,
                                               @RequestParam String checkOutDate) {
        try {
            logger.info("Received request to search hotels in city: {} from {} to {}", cityName, checkInDate, checkOutDate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDate checkInLocalDate = LocalDate.parse(checkInDate, formatter);
            LocalDate checkOutLocalDate = LocalDate.parse(checkOutDate, formatter);
 
            Date checkIn = Date.from(checkInLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date checkOut = Date.from(checkOutLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
 
            List<HotelAndRoomsDTO> results = hotelSearchService.findAvailableHotelsWithRooms(cityName, checkIn, checkOut);
            logger.info("Found {} available hotels for city: {}", results.size(), cityName);
            return results;
        } catch (Exception e) {
            logger.error("Error searching hotels for city: {} - {}", cityName, e.getMessage(), e);
            throw new RuntimeException("Failed to search hotels. Please try again later.");
        }
    }
}