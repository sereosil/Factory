package factory_bd.view;

/**
 * Created by Валерий on 05.08.2016.
 */

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
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
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.Date;

import static factory_bd.view.LoginScreenView.SESSION_USER_KEY;

@SpringView(name = RequestView.VIEW_NAME)
@SpringComponent
@UIScope
public class RequestView extends VerticalLayout implements View {
    private User user;

    public static final String VIEW_NAME = "REQUEST_VIEW";

    java.util.List<Person> personsListTest = new ArrayList<Person>();
    java.util.List<Car> carListTest = new ArrayList<Car>();

    CompanyRepository companyRepository;
    PersonRepository personRepository;
    CarRepository carRepository;

    RequestRepository requestRepository;

    Company company;
    Person person;
    Car car;

    ListSelect companyList = new ListSelect("Выбирите компанию");
    ListSelect personList = new ListSelect("Выбирите сотрудника");
    ListSelect carList = new ListSelect("Выбирите автомобильar");


    PopupDateField dateFrom = new PopupDateField("Начало");
    PopupDateField dateTo = new PopupDateField("Конец");

    TextArea description = new TextArea("Описание");

    Button confirmChoiseButton = new Button("Добавить запрос", FontAwesome.ARCHIVE);

    private  RequestView(){

    }
    @Autowired
    public RequestView(CarRepository carRepository, CompanyRepository companyRepository, PersonRepository personRepository,RequestRepository requestRepository) {
        this.carRepository = carRepository;
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
        this.requestRepository = requestRepository;
        this.personList.isMultiSelect();
        this.carList.isMultiSelect();

    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        init();
    }

    public void listVisualStyleEdit(ListSelect listSelect){
        listSelect.setNullSelectionAllowed(false);
        listSelect.setImmediate(true);
        listSelect.setWidth(600.0f, Unit.PIXELS);
    }


    public void update(){
        companyList.removeAllItems();
        carList.removeAllItems();
        personList.removeAllItems();
        CompanyService companyService = new CompanyService(companyRepository);
        companyService.fillCompanyListInRequest(companyList);
    }

    public void init(){
        dateFrom.setValue(new Date());

        VerticalLayout dateFieldsVerticalLayout = new VerticalLayout(dateFrom,dateTo,description);
        dateFieldsVerticalLayout.setSpacing(true);
        VerticalLayout listLayout = new VerticalLayout(companyList,personList,carList,confirmChoiseButton);
        listLayout.setSpacing(true);
        HorizontalLayout finalLoyout = new HorizontalLayout(listLayout,dateFieldsVerticalLayout);

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
        finalLoyout.setMargin(true);
        finalLoyout.setVisible(true);

        addComponent(finalLoyout);

        companyList.addValueChangeListener(e -> {
            personService.fillPersonListInRequest(personList, (Company) e.getProperty().getValue());
            carService.fillCarListInRequest(carList, (Company) e.getProperty().getValue());
            company = (Company) e.getProperty().getValue();

            personsListTest.clear();
            carListTest.clear();
        });

       personList.addValueChangeListener(e -> {
               person = (Person) e.getProperty().getValue();
               personsListTest.add(person);
       });

        carList.addValueChangeListener( e->{
            car = (Car) e.getProperty().getValue();
            carListTest.add(car);
        });

        confirmChoiseButton.addClickListener( e->{
            try{
                Request request = new Request();

                requestService.createNewRequest(request,company);
                requestService.setCompanyToRequest(request,company);
                requestService.setPersonsList(request,personsListTest);
                requestService.setCarList(request,carListTest);
                requestService.setDateFrom(request,dateFrom.getValue());
                requestService.setDateTo(request,dateTo.getValue());
                requestService.setDescription(request,description.getValue());

                requestService.setApprovedBy(request,user);

                requestService.addRequest(request);

                personsListTest.clear();

                carListTest.clear();
                description.clear();

                Notification.show("Запрос успешно добавлен!",
                        requestService.getRequest(request).toString(),
                        Notification.Type.TRAY_NOTIFICATION.TRAY_NOTIFICATION);
            }
            catch (Throwable t){
                Notification.show("Error!:",
                        t.toString(),
                        Notification.Type.HUMANIZED_MESSAGE.TRAY_NOTIFICATION);
            }
        });
    }

    public interface ChangeHandler {

        void onChange();
    }
    public void setChangeHandler(RequestView.ChangeHandler h) {

    }


}
