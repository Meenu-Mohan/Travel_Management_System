package com.ibsplc.hotelapp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ibsplc.hotelapp.dto.HotelAndRoomsDTO;
import java.util.List;

@FeignClient(value = "hotelavailabilityservice", url = "http://localhost:8081/hotelAvailability")
public interface HotelAvailabilityClient {
 
    @GetMapping("/hotelSearch")
    List<HotelAndRoomsDTO> findAvailableHotelsWithRooms(
            @RequestParam String cityName,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate
    );
}