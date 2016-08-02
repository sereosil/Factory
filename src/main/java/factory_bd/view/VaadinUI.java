package factory_bd.view;

import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import factory_bd.entity.Company;
import factory_bd.repository.CompanyRepository;
import factory_bd.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Валерий on 25.07.2016.
 */
@SpringUI
@Theme("valo")
public class VaadinUI extends UI {
    private final CompanyRepository companyRepo;
    private final PersonRepository personRepo;

    private final CompanyView companyView;
    private final PersonView personView;


    private Company selectedCompany;
    private TabSheet tabSheet = new TabSheet();

    @Autowired
    public VaadinUI(CompanyRepository companyRepo, PersonRepository personRepo, PersonView personView){
        this.companyRepo = companyRepo;
        this.personRepo = personRepo;

        this.companyView = new CompanyView(companyRepo);
        this.personView = new PersonView(personRepo);
        this.companyView.init();
        this.personView.init();

    }

    @Override
    protected void init(VaadinRequest request){

        HorizontalLayout companyLayout = new HorizontalLayout(companyView);
        HorizontalLayout personLayout = new HorizontalLayout(personView);
        HorizontalLayout mainWindowLayout = new HorizontalLayout(companyLayout,personLayout);
        mainWindowLayout.setMargin(true);
        mainWindowLayout.setSpacing(true);
        mainWindowLayout.setVisible(true);

        personLayout.setVisible(false);


        tabSheet.setHeight(500f, Unit.PERCENTAGE);
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tabSheet.addTab(mainWindowLayout, "Company");
        setContent(tabSheet);


        /*
        * Может быть функции?!
        * */
        companyView.companyGrid.addItemClickListener(e->{

                Company clickedCompany = (Company) e.getItemId();
                selectedCompany = clickedCompany;
                personView.selectedCompany = selectedCompany;
                personLayout.setVisible(true);
                personView.fillPersonGridBySelectedCompany(selectedCompany);


        });

        //region BIG "IMPROTANT" COMMENTARY
//        companyGrid.addSelectionListener(e -> {
//            if(e.getSelected().isEmpty()){
//                companyView.setVisible(false);
//                //personLayout.setVisible(false);
//
//            }
//            else {
//                companyView.editCompany((Company) companyGrid.getSelectedRow());
//                personLayout.setVisible(true);
//
//            }
//        });
        /*personGrid.addSelectionListener(e ->{
            if(e.getSelected().isEmpty()){
                personView.setVisible(false);
            }
            else{
                personView.editPerson((Person) personGrid.getSelectedRow());
            }
        });*/


        //addNewCompanyButton.addClickListener(e -> companyView.editCompany(new Company("","","")));
        //addNewPersonButton.addClickListener(e -> personView.editPerson(new Person("","","","")));
       //endregion

        companyView.setChangeHandler(() -> {
            companyView.setVisible(true);
            companyView.fillCompanyGrid(companyView.filterCompany.getValue());
            companyView.setVisible(true);

        });
        personView.setChangeHandler(() -> {
            //personView.fillPersonGrid(personView.filterPerson.getValue(),selectedCompany);//второй парметр
            personView.fillPersonGridBySelectedCompany(selectedCompany);
            personView.setVisible(true);
        });

    }

}
