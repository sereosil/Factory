package factory_bd.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import factory_bd.entity.Company;
import factory_bd.repository.CompanyRepository;
import factory_bd.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by Валерий on 25.07.2016.
 */
@SpringComponent
@UIScope
public class CompanyView extends VerticalLayout implements View  {

    private final CompanyRepository companyRepository;

    private Company company;

    TextField companyName = new TextField("Company name");
    TextField companyAdress = new TextField("Company adress");
    TextField phoneNumber = new TextField("Company phone");
    TextField filterCompany;

    Label searchLabel;

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    Button addNewCompanyButton;
    Grid companyGrid;

    @Autowired
    public CompanyView(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;

        this.addNewCompanyButton = new Button("New company", FontAwesome.PLUS);

        this.filterCompany = new TextField();

        this.companyGrid = new Grid();

        this.searchLabel = new Label("Search:");

    }
    

    public void init(){
        HorizontalLayout companyUpperHorizontalLayout = new HorizontalLayout(searchLabel, filterCompany, addNewCompanyButton);
        companyUpperHorizontalLayout.setSpacing(true);

        VerticalLayout companyMiddleVerticalLayout = new VerticalLayout(companyGrid);

        HorizontalLayout companyActionButtonsLayout = new HorizontalLayout(save, delete, cancel);
        companyActionButtonsLayout.setSpacing(true);

        VerticalLayout companyLowerVerticalLayout = new VerticalLayout( companyName, companyAdress, phoneNumber,companyActionButtonsLayout);
        companyLowerVerticalLayout.setVisible(false);

        VerticalLayout companyFinalVerticalLayout = new VerticalLayout(companyUpperHorizontalLayout, companyMiddleVerticalLayout, companyLowerVerticalLayout);

        companyFinalVerticalLayout.setVisible(true);
        companyFinalVerticalLayout.setSpacing(true);
        companyFinalVerticalLayout.setMargin(true);


        companyGrid.setHeight(300, Sizeable.Unit.PIXELS);
        companyGrid.setColumns("id","companyName","companyAdress","phoneNumber");

        filterCompany.setInputPrompt("Filter by name of company");
        filterCompany.addTextChangeListener( e-> fillCompanyGrid(e.getText()));

        addComponent(companyFinalVerticalLayout);
        addNewCompanyButton.addClickListener(e -> editCompany(new Company("","","")));

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        CompanyService companyService = new CompanyService(companyRepository);

        save.addClickListener(e -> companyService.addCompany(company));
        delete.addClickListener(e -> companyService.deleteCompany(company));
        cancel.addClickListener(e -> editCompany(company));

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
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

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
