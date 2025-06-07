package hotelavailability.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotelavailability.entity.Cities;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<Cities, String> {
   Optional<Cities> findByCityName(String cityName);
}
