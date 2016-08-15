package factory_bd.view;

/**
 * Created by Валерий on 05.08.2016.
 */

import java.util.*;
//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.FontIcon;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import factory_bd.entity.*;
import factory_bd.repository.CarRepository;
import factory_bd.repository.CompanyRepository;
import factory_bd.repository.PersonRepository;
import factory_bd.repository.RequestRepository;
import factory_bd.service.CarService;
import factory_bd.service.CompanyService;
import factory_bd.service.PersonService;
import factory_bd.service.RequestService;
import javafx.beans.binding.ListBinding;
import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;


import javax.xml.bind.ValidationEvent;

import static factory_bd.view.LoginScreenView.SESSION_USER_KEY;

@SpringView(name = RequestView.VIEW_NAME)
@SpringComponent
@UIScope
public class RequestView extends VerticalLayout implements View {
    private User user;

    public static final String VIEW_NAME = "REQUEST_VIEW";

    Set<Person> personsListTest = new HashSet<>();
    Set<Car> carListTest = new HashSet<>();

    CompanyRepository companyRepository;
    PersonRepository personRepository;
    CarRepository carRepository;

    RequestRepository requestRepository;
    Request testRequest;
    Company company;
    Person person;
    Car car;

    ListSelect companyList = new ListSelect("Выберите компанию");
    ListSelect personList = new ListSelect("Выберите сотрудника");
    ListSelect carList = new ListSelect("Выберите автомобиль");


    PopupDateField dateFrom = new PopupDateField("Начало");
    PopupDateField dateTo = new PopupDateField("Конец");

    TextArea description = new TextArea("Описание");

    Button confirmChoiseButton = new Button("Добавить запрос", FontAwesome.ARCHIVE);

    Label informationLabel = new Label("Если отсутствуют необходимые компании, сотрудники или траспортные средства их можно добавить во вкладке \"Контрагенты\" ");

    //Request request;
    private RequestView() {

    }

    @Autowired
    public RequestView(CarRepository carRepository, CompanyRepository companyRepository, PersonRepository personRepository, RequestRepository requestRepository) {
        this.carRepository = carRepository;
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        init();
    }

    public void listVisualStyleEdit(ListSelect listSelect) {
        listSelect.setNullSelectionAllowed(false);
        listSelect.setImmediate(true);
        listSelect.setWidth(600.0f, Unit.PIXELS);
    }


    public void update() {
        companyList.removeAllItems();
        carList.removeAllItems();
        personList.removeAllItems();
        CompanyService companyService = new CompanyService(companyRepository);
        companyService.fillCompanyListInRequest(companyList);
    }

    public void init() {

        dateFrom.setRangeStart(new Date());
        dateTo.setRangeStart(new Date());

        VerticalLayout dateFieldsVerticalLayout = new VerticalLayout(dateFrom, dateTo, description);
        dateFieldsVerticalLayout.setSpacing(true);
        VerticalLayout listLayout = new VerticalLayout(companyList, personList, carList, confirmChoiseButton);
        listLayout.setSpacing(true);
        HorizontalLayout finalLoyout = new HorizontalLayout(listLayout, dateFieldsVerticalLayout);

        VerticalLayout finalVerticalLayout = new VerticalLayout(finalLoyout, informationLabel);

        CompanyService companyService = new CompanyService(companyRepository);
        companyService.fillCompanyListInRequest(companyList);

        PersonService personService = new PersonService(personRepository);
        CarService carService = new CarService(carRepository);

        RequestService requestService = new RequestService(requestRepository);

        companyList.setRows((int) companyRepository.count());
        listVisualStyleEdit(companyList);
        personList.setRows((int) personRepository.count());
        listVisualStyleEdit(personList);
        carList.setRows((int) carRepository.count());
        listVisualStyleEdit(carList);

        finalLoyout.setSpacing(true);

        finalVerticalLayout.setSpacing(true);
        finalVerticalLayout.setMargin(true);
        finalVerticalLayout.setVisible(true);

        addComponent(finalVerticalLayout);

        personList.setMultiSelect(true);
        carList.setMultiSelect(true);


        companyList.addValueChangeListener(e -> {
            personService.fillPersonListInRequest(personList, (Company) e.getProperty().getValue());
            carService.fillCarListInRequest(carList, (Company) e.getProperty().getValue());
            company = (Company) e.getProperty().getValue();
        });

        personList.addValueChangeListener(e -> {
            personsListTest = (Set<Person>) e.getProperty().getValue();
        });

        carList.addValueChangeListener(e -> {
            carListTest = (Set<Car>) e.getProperty().getValue();
        });


        confirmChoiseButton.addClickListener(e -> {



            if (personsListTest.isEmpty() || dateFrom.isEmpty() || dateTo.isEmpty()) {
                Notification.show("Внимание!",
                        "Остались незаполненные поля!",
                        //Notification.Type.TRAY_NOTIFICATION.TRAY_NOTIFICATION);
                        Notification.Type.TRAY_NOTIFICATION.WARNING_MESSAGE);
            } else {
                    final Window window = new Window("Внимание!");
                    window.setWidth(300.0f, Unit.PIXELS);
                    window.setPosition(400, 150);
                    Button ok = new Button("Да");
                    Button no = new Button("Нет");
                    HorizontalLayout buttons = new HorizontalLayout(ok, no);
                    buttons.setSpacing(true);
                    Label areSure = new Label("Вы уверены, что хотите добавить заявку?");
                    final FormLayout content = new FormLayout(areSure, buttons);

                    window.setContent(content);
                    UI.getCurrent().addWindow(window);

                    ok.addClickListener(u -> {
                        try {
                            Request request = new Request();
                            requestService.createNewRequest(request, company);
                            requestService.setCompanyToRequest(request, company);
                            requestService.setPersonsList(request, new ArrayList<>(personsListTest));
                            requestService.setCarList(request, new ArrayList<>(carListTest));
                            requestService.setDateFrom(request, dateFrom.getValue());
                            requestService.setDateTo(request, dateTo.getValue());
                            requestService.setDescription(request, description.getValue());
                            requestService.setCreatedBy(request, user);

                            if(requestService.doRepositoryHaveRequest(request))
                            {
                                Notification.show("Запрос уже существует!!",
                                        requestService.getRequest(request).toString(),
                                        Notification.Type.TRAY_NOTIFICATION.WARNING_MESSAGE);
                            }else {
                                requestService.addRequest(request);
                                Notification.show("Запрос успешно добавлен!",
                                        requestService.getRequest(request).toString(),
                                        Notification.Type.TRAY_NOTIFICATION.TRAY_NOTIFICATION);
                            }
                            dateFrom.clear();
                            dateTo.clear();
                            description.clear();
                        } catch (Throwable t) {
                            Notification.show("Error!:",
                                    t.toString(),
                                    Notification.Type.HUMANIZED_MESSAGE.TRAY_NOTIFICATION);
                        }
                        window.close();
                    });
                    no.addClickListener(u -> {
                        window.close();
                    });

            }


        });
    }

    public void setUser(User user) {
        this.user = user;
    }

    public interface ChangeHandler {

        void onChange();
    }

    public void setChangeHandler(RequestView.ChangeHandler h) {

    }


}
