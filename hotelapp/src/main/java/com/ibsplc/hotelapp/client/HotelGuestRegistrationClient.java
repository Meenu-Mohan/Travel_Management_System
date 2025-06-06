package com.ibsplc.hotelapp.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ibsplc.hotelapp.dto.GuestRequestDTO;
import com.ibsplc.hotelapp.dto.GuestResponseDTO;

@FeignClient(value = "hotelGuestRegistration", url = "http://localhost:8080/guest")
public interface HotelGuestRegistrationClient {
	@PostMapping(value = "/guestRegistration" , consumes = "application/json")
    GuestResponseDTO guestRegistration(@RequestBody GuestRequestDTO guestRequestDTO);
}