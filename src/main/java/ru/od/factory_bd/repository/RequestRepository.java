package ru.od.factory_bd.repository;

import ru.od.factory_bd.entity.Car;
import ru.od.factory_bd.entity.Person;
import ru.od.factory_bd.entity.Request;
import ru.od.factory_bd.entity.User;
import ru.od.factory_bd.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sereo_000 on 20.07.2016.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request,Integer> {
    List<Request> findByCreatedBy(User createdBy);
    List<Request> findByApprovedBy(User approvedBy);
    List<Request> findByCompany(Company company);
    List<Request> findByCompanyOrCarsOrPersons(Company company,Car cars,Person persons);
    List<Request> findByDescription(String description);
    List<Request> findByPersons(Person persons);
    List<Request> findByCars(Car cars);
    List<Request> findByAccepted(boolean condition);
}
