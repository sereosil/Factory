package factory_bd.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import factory_bd.entity.Company;
import factory_bd.entity.Request;
import factory_bd.repository.CarRepository;
import factory_bd.repository.CompanyRepository;
import factory_bd.repository.PersonRepository;
import factory_bd.repository.RequestRepository;
import factory_bd.service.CompanyService;
import factory_bd.service.RequestVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by Валерий on 25.07.2016.
 */
@SpringView (name = CompanyView.VIEW_NAME)
@SpringComponent
@UIScope
public class CompanyView extends VerticalLayout implements View  {
    public static final String VIEW_NAME ="COMPANY_VIEW" ;
    private final CompanyRepository companyRepository;
    private final RequestRepository requestRepository;
    private Company company;

    TextField companyName = new TextField("Название компании");
    TextField companyAdress = new TextField("Адрес");
    TextField phoneNumber = new TextField("Телефон");
    TextField filterCompany;
    Label searchLabel;
    RequestVerifyService requestVerifyService;
    Button save = new Button("Сохранить", FontAwesome.SAVE);
    Button cancel = new Button("Отмена");
    Button delete = new Button("Удалить", FontAwesome.TRASH_O);

    Button addNewCompanyButton;
    Grid companyGrid;

    @Autowired
    public CompanyView(CompanyRepository companyRepository,RequestRepository requestRepository){
        this.companyRepository = companyRepository;
        this.requestRepository = requestRepository;
        this.addNewCompanyButton = new Button("Новая компания", FontAwesome.PLUS);

        this.filterCompany = new TextField();

        this.companyGrid = new Grid();

        this.searchLabel = new Label("Поиск:");

    }


    public void update(){
        CompanyService companyService = new CompanyService(companyRepository);
        companyService.fillCompanyGrid(companyGrid);
    }

    public HorizontalLayout companyActionButtonsLayout = new HorizontalLayout(save, cancel);
    public  VerticalLayout companyLowerVerticalLayout = new VerticalLayout( companyName, companyAdress,
            phoneNumber,companyActionButtonsLayout);

    public void init(){
        requestVerifyService =new RequestVerifyService(requestRepository);
        //HorizontalLayout companyActionButtonsLayout = new HorizontalLayout(save, delete, cancel);

        //VerticalLayout companyLowerVerticalLayout = new VerticalLayout( companyName, companyAdress, phoneNumber,companyActionButtonsLayout);
        companyLowerVerticalLayout.setSpacing(true);

        HorizontalLayout companyUpperHorizontalLayout = new HorizontalLayout(searchLabel, filterCompany, addNewCompanyButton);
        companyUpperHorizontalLayout.setSpacing(true);

        VerticalLayout companyMiddleVerticalLayout = new VerticalLayout(companyGrid);

        companyActionButtonsLayout.setSpacing(true);

        companyLowerVerticalLayout.setVisible(false);

        VerticalLayout companyFinalVerticalLayout = new VerticalLayout(companyUpperHorizontalLayout, companyMiddleVerticalLayout, companyLowerVerticalLayout);

        companyFinalVerticalLayout.setVisible(true);
        companyFinalVerticalLayout.setSpacing(true);
        companyFinalVerticalLayout.setMargin(true);


        companyGrid.setHeight(300, Sizeable.Unit.PIXELS);
        companyGrid.setColumns("id","companyName","companyAdress","phoneNumber");
        companyGrid.getColumn("id").setHeaderCaption("ID");
        companyGrid.getColumn("companyName").setHeaderCaption("Название");
        companyGrid.getColumn("companyAdress").setHeaderCaption("Адрес");
        companyGrid.getColumn("phoneNumber").setHeaderCaption("Телефон");

        filterCompany.setWidth("250");
        filterCompany.setInputPrompt("Поиск по названию компании");
        filterCompany.addTextChangeListener( e-> fillCompanyGrid(e.getText()));

        addComponent(companyFinalVerticalLayout);

        addNewCompanyButton.addClickListener(e -> {
            editCompany(new Company("","",""));
            companyLowerVerticalLayout.setVisible(true);
        });

        save.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        //save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        delete.setStyleName(ValoTheme.BUTTON_DANGER);
        phoneNumber.setIcon(FontAwesome.PHONE);

        CompanyService companyService = new CompanyService(companyRepository);

        save.addClickListener(e -> {
            if(companyService.doRepositoryHaveCompany(company))
            {

            }
            else
            {
                final Window window = new Window("Внимание");
                window.setWidth(300.0f, Unit.PIXELS);
                window.setPosition(400,150);
                Button ok = new Button("Да");
                Button no = new Button("Нет");
                HorizontalLayout buttons = new HorizontalLayout(ok,no);
                buttons.setSpacing(true);
                Label areSure = new Label("Вы уверены, что хотите внести изменения в компанию? В таком случае все предыдущие запросы связанные с данной компанией аннулируются!");
                final FormLayout content = new FormLayout(areSure,buttons);

                window.setContent(content);
                UI.getCurrent().addWindow(window);
                ok.addClickListener(u->{

                    companyService.addCompany(company);
                    testMehod(company.getCompanyName());
                    companyGrid.setContainerDataSource(new BeanItemContainer(Company.class, companyRepository.findAll()));
                    window.close();

                });
                no.addClickListener(u->{
                    window.close();
                });
            }


        });

        delete.addClickListener(e -> {
            companyService.deleteCompany(company);
            companyLowerVerticalLayout.setVisible(false);
        });

        cancel.addClickListener(e -> {
            editCompany(company);
            companyLowerVerticalLayout.setVisible(false);
        });

        companyGrid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                companyLowerVerticalLayout.setVisible(false);

            } else {
                companyLowerVerticalLayout.setVisible(true);
                this.editCompany((Company) companyGrid.getSelectedRow());
            }
        });

        fillCompanyGrid(null);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {init();}

    public interface ChangeHandler {

        void onChange();
    }
    public void fillCompanyGrid(String text){
        if(StringUtils.isEmpty(text)){
            companyGrid.setContainerDataSource(new BeanItemContainer(Company.class, companyRepository.findAll()));
        }
        else {
            companyGrid.setContainerDataSource(new BeanItemContainer(Company.class,
                    companyRepository.findByCompanyNameStartsWithIgnoreCase(text)));
        }
    }
    public void fillCompanyridByAll(){
        companyGrid.setContainerDataSource(new BeanItemContainer(Company.class, companyRepository.findAll()));
    }

    public void testMehod(String text){

        for(Company company:companyRepository.findByCompanyNameStartsWithIgnoreCase(text))
        {
            for(Request request:requestRepository.findByCompany(company)){
                requestVerifyService.setRequestCondition(request,false);
            }
        }

    }


    public final void editCompany(Company c){
        final boolean persisted = c.getId() != null;

        if (persisted){
            company = companyRepository.findOne(c.getId());
        }
        else {
            company = c;
        }
        cancel.setVisible(persisted);

        BeanFieldGroup.bindFieldsUnbuffered(company,this);

        setVisible(true);

        save.focus();

        companyName.selectAll();

    }
    public void setChangeHandler(ChangeHandler h) {
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
}
