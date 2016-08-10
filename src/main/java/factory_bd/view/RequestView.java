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
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import factory_bd.entity.Car;
import factory_bd.entity.Company;
import factory_bd.entity.Person;
import factory_bd.entity.Request;
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

@SpringComponent
@UIScope

public class RequestView extends VerticalLayout implements View {

    java.util.List<Person> personsListTest = new ArrayList<Person>();
    java.util.List<Car> carListTest = new ArrayList<Car>();

    CompanyRepository companyRepository;
    PersonRepository personRepository;
    CarRepository carRepository;

    RequestRepository requestRepository;

    Company company;
    Person person;
    Car car;

    ListSelect companyList = new ListSelect("Select company");
    ListSelect personList = new ListSelect("Select person");
    ListSelect carList = new ListSelect("Select car");


    PopupDateField dateFrom = new PopupDateField("Date from");
    PopupDateField dateTo = new PopupDateField("Date to");

    TextArea description = new TextArea("Description");

    Button confirmChoiseButton = new Button("Add request", FontAwesome.ARCHIVE);

    private  RequestView(){

    }
    @Autowired
    public RequestView(CarRepository carRepository, CompanyRepository companyRepository, PersonRepository personRepository,RequestRepository requestRepository) {
        this.carRepository = carRepository;
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
        this.requestRepository = requestRepository;

    }


    public void listVisualStyleEdit(ListSelect listSelect){
        listSelect.setNullSelectionAllowed(false);
        listSelect.setImmediate(true);
        listSelect.setWidth(600.0f, Unit.PIXELS);
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
                requestService.addRequest(request);

                personsListTest.clear();

                carListTest.clear();
                description.clear();

                Notification.show("Request successfully added!",
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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
