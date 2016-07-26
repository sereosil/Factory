package factory_bd.repository;

import java.util.List;

import factory_bd.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Created by Валерий on 20.07.2016.
 */
public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findByCarColorStartsWithIgnoreCase(String carColor);
    List<Car> findByCarRegistrationNumberStartsWithIgnoreCase(String carRegistrationNumber);
    List<Car> findByCarModelStartsWithIgnoreCase(String carModel);
}
