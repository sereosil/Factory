package factory_bd.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import factory_bd.entity.Company;
import factory_bd.entity.User;
import factory_bd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import static factory_bd.view.LoginScreenView.SESSION_USER_KEY;

/**
 * Created by Валерий on 10.08.2016.
 */
@SpringView(name = SpecialView.VIEW_NAME)//ВЕЗДЕ!!!!!!!
@SpringComponent
@UIScope
public class SpecialView  extends VerticalLayout implements View {
    public static final String VIEW_NAME ="SPECIAL_VIEW" ;
    private User user;
    private final CompanyRepository companyRepo;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;


    private final PersonRepository personRepo;
    private final CarRepository carRepo;
    private final RequestRepository requestRepo;
    private final CompanyView companyView;
    private final PersonView personView;
    private final CarView carView;
    private final RequestView requestView;
    private final RequestVerifyView requestVerifyView;
    private final SecurityView securityView;

 /*   private final AdminWindowView adminWindowView;
    private final UserSettingsView userSettingsView;
*/
    private TabSheet tabSheet = new TabSheet();

    private Company selectedCompany;

    @Autowired
    public SpecialView(CompanyRepository companyRepo, UserRepository userRepository, UserRoleRepository userRoleRepository, PersonRepository personRepo, CarRepository carRepo, RequestRepository requestRepo) {
        this.companyRepo = companyRepo;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.personRepo = personRepo;
        this.carRepo = carRepo;
        this.requestRepo = requestRepo;


        this.companyView = new CompanyView(companyRepo, personRepo, carRepo, requestRepo);
        this.personView = new PersonView(personRepo);
        this.carView = new CarView(carRepo);
        this.requestVerifyView = new RequestVerifyView(requestRepo);
        this.securityView = new SecurityView(requestRepo,companyRepo);
        this.requestView = new RequestView(carRepo, companyRepo, personRepo, requestRepo);

      /*  this.adminWindowView = new AdminWindowView(userRepository,userRoleRepository);
        this.userSettingsView = new UserSettingsView(userRepository,userRoleRepository);*/


        this.companyView.init();
        this.personView.init();
        this.carView.init();
        this.requestView.init();
        this.requestVerifyView.init();
        this.securityView.init();
       /* this.adminWindowView.init();
        this.userSettingsView.init();*/
    }

    public void init(){
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
        tabSheet.addTab(securityView,"Охрана");
        tabSheet.addTab(requestVerifyView,"Подтверждение запросов");
        tabSheet.addTab(requestView,"Создание запроса");
        tabSheet.addTab(mainWindowLayout, "Редактирование БД");
        /*tabSheet.addTab(adminWindowView, "Администратор");
        tabSheet.addTab(userSettingsView, "Роли пользователей");*/

        tabSheet.addSelectedTabChangeListener( e -> {
            Notification.show("Tab changed",
                    "вв",
                    Notification.Type.TRAY_NOTIFICATION.TRAY_NOTIFICATION);
            requestVerifyView.update();
            securityView.update();
            requestView.update();

        });
        addComponent(tabSheet);

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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        init();
    }
}
