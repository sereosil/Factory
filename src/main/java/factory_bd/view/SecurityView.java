package factory_bd.view;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import factory_bd.entity.Company;
import factory_bd.entity.Request;
import factory_bd.entity.User;
import factory_bd.repository.CompanyRepository;
import factory_bd.repository.RequestRepository;
import factory_bd.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import static factory_bd.view.LoginScreenView.SESSION_USER_KEY;

/**
 * Created by Валерий on 10.08.2016.
 */
@SpringView (name = SecurityView.VIEW_NAME)
@SpringComponent
@UIScope
public class SecurityView extends VerticalLayout implements View {
    public static final String VIEW_NAME ="SECURITY_VIEW" ;
    private User user;
    RequestRepository requestRepository;
    CompanyRepository companyRepository;

    TextField filterRequest;
    Label searchLabel;
    Grid securityGrid;

    @Autowired
    public SecurityView(RequestRepository requestRepository, CompanyRepository companyRepository) {
        this.requestRepository = requestRepository;
        this.companyRepository = companyRepository;
        this.securityGrid = new Grid();
        this.filterRequest = new TextField();
        this.searchLabel = new Label("Поиск:");

    }
    public void update(){

        RequestService requestService = new RequestService(requestRepository);
        requestService.fillRequestGrid(securityGrid);
    }

    public void init(){
        HorizontalLayout searchLayout = new HorizontalLayout(searchLabel,filterRequest);
        VerticalLayout mainLayout = new VerticalLayout(searchLayout,securityGrid);
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setVisible(true);
        securityGrid.setWidth(600f, Unit.PIXELS);
        securityGrid.setSizeFull();
        securityGrid.setColumns("id","company","persons","cars","dateFrom","dateTo","approvedBy");
        securityGrid.getColumn("id").setHeaderCaption("ID");
        securityGrid.getColumn("company").setHeaderCaption("Компания");
        securityGrid.getColumn("persons").setHeaderCaption("Сотрудники");
        securityGrid.getColumn("cars").setHeaderCaption("Автомобили");
        securityGrid.getColumn("dateFrom").setHeaderCaption("От");
        securityGrid.getColumn("dateTo").setHeaderCaption("До");
        securityGrid.getColumn("approvedBy").setHeaderCaption("Подтвержено");

        filterRequest.addTextChangeListener( e-> fillSecurityGridByFindedCompany(e.getText()));

        RequestService requestService = new RequestService(requestRepository);
        requestService.fillRequestGrid(securityGrid);
        addComponent(mainLayout);

    }

    public void fillSecurityGridByFindedCompany(String text){
        if(StringUtils.isEmpty(text)){
            securityGrid.setContainerDataSource(new BeanItemContainer(Request.class, requestRepository.findAll()));
        }
        else {
                for(Company company:companyRepository.findByCompanyNameStartsWithIgnoreCase(text))
                {
                    securityGrid.setContainerDataSource(new BeanItemContainer(Request.class,
                            requestRepository.findByCompany(company)));
                }
        }
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        init();
    }
}
