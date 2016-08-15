package factory_bd.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import factory_bd.entity.Company;
import factory_bd.entity.Person;
import factory_bd.entity.Request;
import factory_bd.entity.User;
import factory_bd.repository.CompanyRepository;
import factory_bd.repository.PersonRepository;
import factory_bd.repository.RequestRepository;
import factory_bd.service.PersonService;
import factory_bd.service.RequestVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by Валерий on 27.07.2016.
 */
@SpringView(name = PersonView.VIEW_NAME)
@SpringComponent
@UIScope
public class PersonView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "PERSON_VIEW";

    private User user;
    private final PersonRepository personRepository;
    private final RequestRepository requestRepository;
    private final CompanyRepository companyRepository;
    private Person person;
    public Company selectedCompany;
    RequestVerifyService requestVerifyService;
    TextField firstName = new TextField("Имя");
    TextField lastName = new TextField("Фамилия");
    TextField passportIdentification = new TextField("Номер паспорта");
    TextField filterPerson;

    Label searchLabel;

    Button save = new Button("Сохранить", FontAwesome.SAVE);
    Button cancel = new Button("Отмена");
    Button delete = new Button("Удалить", FontAwesome.TRASH_O);
    Button addNewPersonButton;

    Grid personGrid;

    @Autowired
    public PersonView(PersonRepository personRepository, RequestRepository requestRepository, CompanyRepository companyRepository) {
        this.personRepository = personRepository;
        this.requestRepository = requestRepository;
        this.companyRepository = companyRepository;
        this.addNewPersonButton = new Button("Новый сотрудник", FontAwesome.PLUS);
        this.filterPerson = new TextField();
        this.personGrid = new Grid();
        this.searchLabel = new Label("Поиск:");

    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        // this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        init();
    }


    public void update() {
        PersonService personService = new PersonService(personRepository);
        personService.fillPersonGrid(personGrid, selectedCompany);
    }

    public void init() {
        personGrid.setHeight(300, Unit.PIXELS);
        personGrid.setColumns("id", "firstName", "lastName", "passportIdentification");
        requestVerifyService = new RequestVerifyService(requestRepository);

        personGrid.getColumn("id").setHeaderCaption("ID");
        personGrid.getColumn("firstName").setHeaderCaption("Имя");
        personGrid.getColumn("lastName").setHeaderCaption("Фамилия");
        personGrid.getColumn("passportIdentification").setHeaderCaption("Паспорт");


        filterPerson.setInputPrompt("Поиск по фамилии");
        filterPerson.addTextChangeListener(e -> fillPersonGridByLastName(e.getText(), selectedCompany));


        HorizontalLayout personUpperHorizontalLayout = new HorizontalLayout(searchLabel, filterPerson, addNewPersonButton);
        personUpperHorizontalLayout.setSpacing(true);

        VerticalLayout personMiddleVerticalLayout = new VerticalLayout(personGrid);

        HorizontalLayout personActionButtonsLayout = new HorizontalLayout(save, cancel);
        personActionButtonsLayout.setSpacing(true);

        VerticalLayout personLowerVerticalLayout = new VerticalLayout(firstName, lastName, passportIdentification, personActionButtonsLayout);
        personLowerVerticalLayout.setSpacing(true);
        personLowerVerticalLayout.setVisible(false);

        VerticalLayout personFinalVerticalLayout = new VerticalLayout(personUpperHorizontalLayout,
                personMiddleVerticalLayout, personLowerVerticalLayout);

        personFinalVerticalLayout.setVisible(true);
        personFinalVerticalLayout.setMargin(true);
        personFinalVerticalLayout.setSpacing(true);

        addComponent(personFinalVerticalLayout);


        personGrid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                personLowerVerticalLayout.setVisible(false);
            } else {
                personLowerVerticalLayout.setVisible(true);
                this.editPerson((Person) personGrid.getSelectedRow());
            }
        });


        addNewPersonButton.addClickListener(e -> {
            editPerson(new Person("", "", selectedCompany, ""));
            personLowerVerticalLayout.setVisible(true);

        });

        save.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        delete.setStyleName(ValoTheme.BUTTON_DANGER);

        PersonService personService = new PersonService(personRepository);

        save.addClickListener(e -> {
            if (personService.doRepositoryHavePerson(person)) {
                Notification.show("Внимание!",
                    "Сотрудник с данными номером паспорта уже существует!",
                    Notification.Type.TRAY_NOTIFICATION.WARNING_MESSAGE);
            }
            else {

                final Window window = new Window("Внимание");
                window.setWidth(300.0f, Unit.PIXELS);
                window.setPosition(400, 150);
                Button ok = new Button("Да");
                Button no = new Button("Нет");
                HorizontalLayout buttons = new HorizontalLayout(ok, no);
                buttons.setSpacing(true);
                Label areSure = new Label("Вы уверены, что хотите изменить параметры выбранного сотрудника? В таком случае все предыдущие запросы связанные с ним аннулируются!");
                final FormLayout content = new FormLayout(areSure, buttons);
                window.setContent(content);
                UI.getCurrent().addWindow(window);
                ok.addClickListener(u -> {

                    personService.addPeson(person);
                    testMehod(selectedCompany.getCompanyName());
                    personLowerVerticalLayout.setVisible(false);
                    fillPersonGridBySelectedCompany(selectedCompany);
                    window.close();

                });
                no.addClickListener(u -> {
                    window.close();
                });
            }


        });
        delete.addClickListener(e -> {
            personService.deletePerson(person);
            personLowerVerticalLayout.setVisible(false);
        });
        cancel.addClickListener(e -> {
            editPerson(person);
            personLowerVerticalLayout.setVisible(false);
        });
    }

    public interface ChangeHandler {

        void onChange();
    }


    public void fillPersonGridByLastName(String text, Company selectedCompany) {
        if (StringUtils.isEmpty(text)) {
            personGrid.setContainerDataSource(new BeanItemContainer(Person.class,
                    personRepository.findByCompanyName(selectedCompany.getCompanyName())));
        } else {
            personGrid.setContainerDataSource(new BeanItemContainer(Person.class,
                    personRepository.findByLastNameStartsWithIgnoreCase(text)));
        }
    }

    public void fillPersonGridBySelectedCompany(Company selectedCompany) {
        if (selectedCompany == null) {
            personGrid.setContainerDataSource(new BeanItemContainer(Person.class,
                    personRepository.findByCompany(selectedCompany)));
        } else {
            personGrid.setContainerDataSource(new BeanItemContainer(Person.class,
                    personRepository.findByCompany(selectedCompany)));
        }
    }

    public void testMehod(String text) {
        for (Company company : companyRepository.findByCompanyNameStartsWithIgnoreCase(selectedCompany.getCompanyName())) {
            for (Person person : personRepository.findByCompany(company)) {
                for (Request request : requestRepository.findByPersons(person)) {
                    requestVerifyService.setRequestCondition(request, false);
                }
            }
        }


    }

    public final void editPerson(Person p) {
        final boolean persisted = p.getId() != null;

        if (persisted) {
            person = personRepository.findOne(p.getId());
        } else {
            person = p;
        }
        cancel.setVisible(persisted);

        BeanFieldGroup.bindFieldsUnbuffered(person, this);

        setVisible(true);

        save.focus();

        firstName.selectAll();

    }

    public void setChangeHandler(PersonView.ChangeHandler h) {
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }


}
