package com.ibsplc.hotelapp.repo;


import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibsplc.hotelapp.entity.Guests;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestsRepository extends JpaRepository<Guests, Long> {

    /**
     * Finds a guest by their email address.
     *
     * @param email The email address to search for.
     * @return An Optional containing the guest if found, otherwise empty.
     */
    Optional<Guests> findByEmail(String email);

    /**
     * Finds a guest by their phone number.
     *
     * @param phone The phone number to search for.
     * @return An Optional containing the guest if found, otherwise empty.
     */
    Optional<Guests> findByPhone(String phone);
}