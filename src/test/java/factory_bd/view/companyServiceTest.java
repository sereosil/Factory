package factory_bd.view;

import factory_bd.entity.Company;
import factory_bd.repository.CompanyRepository;
import factory_bd.service.CompanyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Валерий on 25.07.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {

    @Mock
    CompanyRepository companyRepository;

    private Company c;

    @Before
    public void setUp() {

       c = new Company("qwer","123","234");
        when(companyRepository.findByCompanyNameStartsWithIgnoreCase("qwer")).thenReturn(Arrays.asList(new Company []{c}));
    }
    @Test
    public void haveCompanyTest(){
        CompanyService compServ = new CompanyService(companyRepository);
        boolean b = compServ.doRepositoryHaveCompany("qwer");
    }
    @Test
    public void tryToChangeCompanyAdress(){
       /* CompanyService compServ = new CompanyService(companyRepository);
        compServ.addCompany(c);
        compServ.changeCompanyAdress(c,"321");
        when(companyRepository.findAll()).thenReturn(Arrays.asList(new Company []{c}));*/

    }


}