package com.ibsplc.hotelbatchmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ibsplc.hotelbatchmanagement.entity.Rooms;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, String> {
}