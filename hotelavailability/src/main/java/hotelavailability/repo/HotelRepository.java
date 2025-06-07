package hotelavailability.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotelavailability.entity.Hotels;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotels, String> {
    List<Hotels> findByCityId(String cityId);
}
