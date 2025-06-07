package com.ibsplc.hotelbatchmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ibsplc.hotelbatchmanagement.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, String> {

}