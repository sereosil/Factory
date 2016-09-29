package ru.od.factory_bd.repository;

import java.util.List;

import ru.od.factory_bd.entity.Car;
import ru.od.factory_bd.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Валерий on 20.07.2016.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findByCarColorStartsWithIgnoreCase(String carColor);
    List<Car> findByCarRegistrationNumberStartsWithIgnoreCase(String carRegistrationNumber);
    List<Car> findByCarModelStartsWithIgnoreCase(String carModel);
    List<Car> findByCompanyName (String companyName);
    List<Car> findByCompany (Company company);

}
