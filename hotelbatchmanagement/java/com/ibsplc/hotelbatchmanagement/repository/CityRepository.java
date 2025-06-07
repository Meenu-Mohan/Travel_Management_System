package com.ibsplc.hotelbatchmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ibsplc.hotelbatchmanagement.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, String> {
}