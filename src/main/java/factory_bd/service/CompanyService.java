package factory_bd.service;


import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ListSelect;
import factory_bd.entity.Company;
import factory_bd.entity.Person;
import factory_bd.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Валерий on 22.07.2016.
 */
@SpringComponent
public class CompanyService {
    private final CompanyRepository companyRepository ;

    private Company company;

    @Autowired//внедрение зависимостей - в
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;

    }
    @Deprecated
    public long getCountOfCompanies(){
        return companyRepository.count();
    }

    public boolean doRepositoryHaveCompany(String companyName){
        for (Company comp:companyRepository.findByCompanyNameStartsWithIgnoreCase(companyName)){
            return true;
        }
        return false;
    }
    public void addCompany(Company companyToAdd){
        companyRepository.save(companyToAdd);
    }

    public void deleteCompany(Company companyToRemove){
        companyRepository.delete(companyToRemove);
    }

    public void changeCompanyAdress(Company company,String newAdress) {
        company.setCompanyAdress(newAdress);
        companyRepository.save(company);
    }
    public  void changeCompanyPhoneNumber(Company company,String newCompanyPhoneNumber){
        company.setPhoneNumber(newCompanyPhoneNumber);
        companyRepository.save(company);
    }

    public void fillCompanyListInRequest(ListSelect listSelect){
        for (Company company : companyRepository.findAll()) {
            listSelect.addItem(company);
        }

    }




}
