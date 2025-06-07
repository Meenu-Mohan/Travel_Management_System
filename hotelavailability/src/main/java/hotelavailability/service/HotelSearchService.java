package hotelavailability.service;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hotelavailability.dto.HotelAndRoomsDTO;
import hotelavailability.entity.Cities;
import hotelavailability.entity.Hotels;
import hotelavailability.entity.RoomAvailability;
import hotelavailability.entity.Rooms;
import hotelavailability.repo.CityRepository;
import hotelavailability.repo.HotelRepository;
import hotelavailability.repo.RoomAvailabilityRepository;
import hotelavailability.repo.RoomRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 

@Service
public class HotelSearchService {

   private static final Logger logger = LogManager.getLogger(HotelSearchService.class);

   @Autowired
   private HotelRepository hotelRepository;

   @Autowired
   private RoomRepository roomRepository;

   @Autowired
   private CityRepository cityRepository;

   @Autowired
   private RoomAvailabilityRepository roomAvailabilityRepository;

   public List<HotelAndRoomsDTO> findAvailableHotelsWithRooms(String cityName, Date checkInDate, Date checkOutDate) {
       try {
           logger.info("Searching available hotels in city: {} from {} to {}", cityName, checkInDate, checkOutDate);
           Optional<Cities> cityIdFetched = cityRepository.findByCityName(cityName);
           if (cityIdFetched.isEmpty()) {
               logger.warn("City not found: {}", cityName);
               throw new RuntimeException("City not found: " + cityName);
           }
           String cityId = cityIdFetched.get().getCityId();
           List<Hotels> hotels = hotelRepository.findByCityId(cityId);

           List<HotelAndRoomsDTO> results = hotels.stream().map(hotel -> {
               List<Rooms> rooms = roomRepository.findByHotelId(hotel.getHotelId());
               List<String> roomIds = rooms.stream().map(Rooms::getRoomId).collect(Collectors.toList());

               List<RoomAvailability> availabilities = roomAvailabilityRepository.findByRoomIdsAndDateBetween(
                       roomIds, checkInDate, checkOutDate
               );

               List<Rooms> availableRooms = rooms.stream()
                   .filter(room -> availabilities.stream()
                       .anyMatch(availability -> availability.getRoomId().equals(room.getRoomId())))
                   .collect(Collectors.toList());

               // Log the available rooms with their maxCapacity for debugging
               availableRooms.forEach(room -> 
                   logger.debug("Hotel: {}, Room Type: {}, Max Capacity: {}", 
                       hotel.getHotelName(), room.getRoomType(), room.getMaxCapacity())
               );

               return new HotelAndRoomsDTO(hotel, availableRooms);
           }).filter(dto -> !dto.getAvailableRooms().isEmpty())
           .collect(Collectors.toList());

           logger.info("Found {} available hotels in city: {}", results.size(), cityName);
           return results;
       } catch (Exception e) {
           logger.error("Error searching available hotels in city: {} - {}", cityName, e.getMessage(), e);
           throw new RuntimeException("Failed to search hotels.");
       }
   }
}