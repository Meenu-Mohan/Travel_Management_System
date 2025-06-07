package hotelbatchmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotelbatchmanagement.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, String> {
}