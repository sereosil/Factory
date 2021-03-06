package ru.od.factory_bd.service;


import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ListSelect;
import ru.od.factory_bd.entity.Company;
import ru.od.factory_bd.entity.Request;
import ru.od.factory_bd.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Валерий on 22.07.2016.
 */
@SpringComponent
public class CompanyService {
    private final CompanyRepository companyRepository ;

    private Company company;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;

    }
    @Deprecated
    public long getCountOfCompanies(){
        return companyRepository.count();
    }

  /*  public boolean doRepositoryHaveCompany(String companyName){
        for (Company comp:companyRepository.findByCompanyNameStartsWithIgnoreCase(companyName)){
            return true;
        }
        return false;
    } */
    public boolean doRepositoryHaveCompany(Company company){
        boolean check = false;
        for (Company comp:companyRepository.findAll()){
            if( comp.getCompanyName().equals(company.getCompanyName())){
                check = true;
                break;
            }
            else {
                check = false;
            }
        }
        return check;
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
    public void fillCompanyGrid(Grid grid)
    {
        grid.setContainerDataSource( new BeanItemContainer(Request.class,companyRepository.findAll()));
    }


}
