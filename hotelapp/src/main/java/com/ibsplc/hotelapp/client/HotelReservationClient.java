package com.ibsplc.hotelapp.client;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ibsplc.hotelapp.dto.ReservationRequestDTO;
import com.ibsplc.hotelapp.dto.ReservationResponseDTO;

@FeignClient(value = "hotelreservationmanagement", url = "http://localhost:8087/hotelBooking")
public interface HotelReservationClient {
	
    @PostMapping(value = "/hotelReservation" , consumes = "application/json")
    ReservationResponseDTO createReservation(@RequestBody ReservationRequestDTO requestDTO);
    
    @GetMapping("/reservationsByGuest")
    List<ReservationResponseDTO> getReservationsByGuest(@RequestParam("email") String email, @RequestParam("phone") String phone);
}