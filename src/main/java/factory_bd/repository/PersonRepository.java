package factory_bd.repository;

import factory_bd.entity.Person;
import factory_bd.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Валерий on 20.07.2016.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person,Integer>{

    List<Person> findByCompanyName (Company companyName);
    //List<Person> findByCompany(Company company);
}
