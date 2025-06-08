package com.ibsplc.flightbatchmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ibsplc.flightbatchmanagement.entity.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {
}