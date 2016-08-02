package factory_bd.repository;

import factory_bd.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Валерий on 20.07.2016.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer>{
    List<Company> findByCompanyNameStartsWithIgnoreCase(String companyName);
}
