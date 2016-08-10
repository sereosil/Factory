package factory_bd.view;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import factory_bd.entity.Company;
import factory_bd.repository.CarRepository;
import factory_bd.repository.CompanyRepository;
import factory_bd.repository.PersonRepository;
import factory_bd.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Валерий on 25.07.2016.
 */
@SpringUI
@Theme("valo")
public class VaadinUI extends UI {
    private final CompanyRepository companyRepo;
    private final PersonRepository personRepo;
    private final CarRepository carRepo;
    private final RequestRepository requestRepo;

    private final CompanyView companyView;
    private final PersonView personView;
    private final CarView carView;
    private final RequestView requestView;
    private final RequestVerifyView requestVerifyView;
    private final SecurityView securityView;

    private Company selectedCompany;
    private TabSheet tabSheet = new TabSheet();

    @Autowired
    public VaadinUI(CompanyRepository companyRepo, PersonRepository personRepo, CarRepository carRepo, RequestRepository requestRepo){
        this.companyRepo = companyRepo;
        this.personRepo = personRepo;
        this.carRepo = carRepo;
        this.requestRepo = requestRepo;

        this.companyView = new CompanyView(companyRepo);
        this.personView = new PersonView(personRepo);
        this.carView = new CarView(carRepo);
        this.requestVerifyView = new RequestVerifyView(requestRepo);
        this.securityView = new SecurityView(requestRepo);
        this.requestView = new RequestView(carRepo, companyRepo, personRepo, requestRepo);

        this.companyView.init();
        this.personView.init();
        this.carView.init();
        this.requestView.init();
        this.requestVerifyView.init();
        this.securityView.init();
    }

    @Override
    protected void init(VaadinRequest request){

        HorizontalLayout companyLayout = new HorizontalLayout(companyView);
        HorizontalLayout personLayout = new HorizontalLayout(personView);
        HorizontalLayout carLayout = new HorizontalLayout(carView);

      /*  HorizontalLayout requestLayout = new HorizontalLayout(requestView);
        requestLayout.setVisible(true);*/

        HorizontalLayout mainWindowLayout = new HorizontalLayout(companyLayout,personLayout,carLayout);
        mainWindowLayout.setMargin(true);
        mainWindowLayout.setSpacing(true);
        mainWindowLayout.setVisible(true);

        personLayout.setVisible(false);
        carLayout.setVisible(false);

        tabSheet.setHeight(500f, Unit.PERCENTAGE);
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tabSheet.addTab(securityView,"Security");
        tabSheet.addTab(requestVerifyView,"Verify request");
        tabSheet.addTab(requestView,"Request");
        tabSheet.addTab(mainWindowLayout, "Company");

        setContent(tabSheet);

        companyView.companyGrid.addItemClickListener(e->{

                Company clickedCompany = (Company) e.getItemId();
                selectedCompany = clickedCompany;

                personView.selectedCompany = selectedCompany;
                carView.selectedCompany = selectedCompany;

                personLayout.setVisible(true);
                carLayout.setVisible(true);

                personView.fillPersonGridBySelectedCompany(selectedCompany);
                carView.fillCarGridBySelectedCompany(selectedCompany);

        });

        companyView.companyGrid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                personLayout.setVisible(false);
                carLayout.setVisible(false);

            } else {
                companyView.setVisible(true);
                companyView.editCompany((Company) companyView.companyGrid.getSelectedRow());

            }
        });



        companyView.setChangeHandler(() -> {
            companyView.fillCompanyGrid(companyView.filterCompany.getValue());
            companyView.setVisible(true);

        });

        personView.setChangeHandler(() -> {
            personView.fillPersonGridBySelectedCompany(selectedCompany);
            personView.setVisible(true);
        });

        carView.setChangeHandler(() ->{
            carView.fillCarGridBySelectedCompany(selectedCompany);
            carView.setVisible(true);
        });

    }

}
