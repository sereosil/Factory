package ru.od.factory_bd.repository;

import ru.od.factory_bd.entity.Person;
import ru.od.factory_bd.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Валерий on 20.07.2016.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person,Integer>{

    List<Person> findByCompanyName (String companyName);
    List<Person> findByFirstNameStartsWithIgnoreCase (String firstName);
    List<Person> findByLastNameStartsWithIgnoreCase (String firstName);
    List<Person> findByFirstNameStartsWithIgnoreCaseAndCompany (String firstName, Company company);
    List<Person> findByCompany(Company company);
}
