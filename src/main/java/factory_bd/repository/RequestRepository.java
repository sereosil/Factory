package factory_bd.repository;

import factory_bd.entity.Car;
import factory_bd.entity.Person;
import factory_bd.entity.Request;
import factory_bd.entity.User;
import factory_bd.entity.Company;
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
    List<Request> findByDescription(String description);
    List<Request> findByPersons(Person persons);
    List<Request> findByCars(Car cars);
}
