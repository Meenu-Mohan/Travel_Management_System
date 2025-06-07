package hotelreservationmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotelreservationmanagement.entity.Guests;

@Repository
public interface GuestsRepository extends JpaRepository<Guests, Long> {
    Optional<Guests> findByEmailAndPhone(String email, String phone);
    Optional<Guests> findByEmailOrPhone(String email, String phone);
}