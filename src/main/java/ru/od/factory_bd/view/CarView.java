package ru.od.factory_bd.view;

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
import ru.od.factory_bd.entity.Car;
import ru.od.factory_bd.entity.Company;
import ru.od.factory_bd.entity.Request;
import ru.od.factory_bd.entity.User;
import ru.od.factory_bd.repository.CarRepository;
import ru.od.factory_bd.repository.CompanyRepository;
import ru.od.factory_bd.repository.RequestRepository;
import ru.od.factory_bd.service.CarService;
import ru.od.factory_bd.service.RequestVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


/**
 * Created by Валерий on 01.08.2016.
 */
@SpringView(name = CarView.VIEW_NAME)
@SpringComponent
@UIScope
public class CarView extends VerticalLayout implements View{
    private User user;

    public static final String VIEW_NAME = "CAR_VIEW";
    private final CarRepository carRepository;
    private final RequestRepository requestRepository;
    private final CompanyRepository companyRepository;
    private Car car;
    public Company selectedCompany;

    TextField carModel = new TextField("Марка/модель");
    TextField carColor = new TextField("Цвет");
    TextField carRegistrationNumber = new TextField("Рег. №");
    TextField filterCar;

    RequestVerifyService requestVerifyService;
    Label searchLabel;

    Button save = new Button("Сохранить", FontAwesome.SAVE);
    Button cancel = new Button("Отмена");
    Button delete = new Button("Удалить", FontAwesome.TRASH_O);
    Button addNewCarButton;

    Grid carGrid;


    @Autowired
    public CarView(CarRepository carRepository, RequestRepository requestRepository, CompanyRepository companyRepository) {
        this.carRepository = carRepository;
        this.requestRepository = requestRepository;
        this.companyRepository = companyRepository;
        this.addNewCarButton = new Button("Новый автомобиль", FontAwesome.PLUS);
        this.filterCar = new TextField( );
        this.carGrid = new Grid();
        this.searchLabel = new Label("Поиск:");
    }
    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
      //  this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        init();
    }
    public void update(){


        CarService carService = new CarService(carRepository);
        carService.fillCarGrid(carGrid,selectedCompany);
    }

    public void init(){
        requestVerifyService =new RequestVerifyService(requestRepository);
        carGrid.setHeight(300,Unit.PIXELS);
        carGrid.setColumns("id","carModel","carColor","carRegistrationNumber");
        carGrid.getColumn("id").setHeaderCaption("ID");
        carGrid.getColumn("carModel").setHeaderCaption("Марка/модель");
        carGrid.getColumn("carColor").setHeaderCaption("Цвет");
        carGrid.getColumn("carRegistrationNumber").setHeaderCaption("Рег.№");

        filterCar.setInputPrompt("Поиск по марке/модели");

        CarService carService = new CarService(carRepository);

        HorizontalLayout carUpperHorizontalLayout = new HorizontalLayout(searchLabel,filterCar,addNewCarButton);
        carUpperHorizontalLayout.setSpacing(true);

        VerticalLayout carMiddleVerticalLayout = new VerticalLayout(carGrid);

        HorizontalLayout carActionButtonsLayout = new HorizontalLayout(save,cancel);
        carActionButtonsLayout.setSpacing(true);

        VerticalLayout carLowerVerticalLayout = new VerticalLayout(carModel,carColor,carRegistrationNumber, carActionButtonsLayout);
        carLowerVerticalLayout.setSpacing(true);
        carLowerVerticalLayout.setVisible(false);

        VerticalLayout carFinalVerticalLayout = new VerticalLayout(carUpperHorizontalLayout,carMiddleVerticalLayout,carLowerVerticalLayout);

        carFinalVerticalLayout.setVisible(true);
        carFinalVerticalLayout.setSpacing(true);
        carFinalVerticalLayout.setMargin(true);

        addComponent(carFinalVerticalLayout);

        carGrid.addSelectionListener( e->{
            if(e.getSelected().isEmpty()){
                carLowerVerticalLayout.setVisible(false);
            }
            else{
                carLowerVerticalLayout.setVisible(true);
                this.editCar((Car) carGrid.getSelectedRow());
            }

        });

        addNewCarButton.addClickListener(e -> {
            editCar(new Car("","","",selectedCompany));
            carLowerVerticalLayout.setVisible(true);
        });
        filterCar.addTextChangeListener(e -> fillCarGridByCarModel(e.getText(),selectedCompany));

        save.addClickListener(e->{
            if (carService.doRepositoryHaveCar(car)){
                Notification.show("Внимание!",
                        "Транспротное средство с данными регистрационным номером уже существует!",
                        //Notification.Type.TRAY_NOTIFICATION.TRAY_NOTIFICATION);
                        Notification.Type.TRAY_NOTIFICATION.WARNING_MESSAGE);
            }
            else {
                final Window window = new Window("Внимание");
                window.setWidth(300.0f, Unit.PIXELS);
                window.setPosition(400,150);
                Button ok = new Button("Да");
                Button no = new Button("Нет");
                HorizontalLayout buttons = new HorizontalLayout(ok,no);
                buttons.setSpacing(true);
                Label areSure = new Label("Вы уверены, что хотите изменить параметры выбранного транспортного средства? В таком случае все предыдущие запросы связанные с ним аннулируются!");
                final FormLayout content = new FormLayout(areSure,buttons);

                window.setContent(content);
                UI.getCurrent().addWindow(window);
                ok.addClickListener(u->{
                    carService.addCar(car);
                    testMehod(selectedCompany.getCompanyName());
                    carLowerVerticalLayout.setVisible(false);
                    fillCarGridBySelectedCompany(selectedCompany);
                    window.close();
                });
                no.addClickListener(u->{
                    window.close();
                });
            }



        });
        delete.addClickListener(e->{
            carService.deleteCar(car);
            carLowerVerticalLayout.setVisible(false);
        });
        cancel.addClickListener(e->{
            editCar(car);
            carLowerVerticalLayout.setVisible(false);
        });

        save.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        //save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        delete.setStyleName(ValoTheme.BUTTON_DANGER);
    }

    public interface ChangeHandler {

        void onChange();
    }

    public void testMehod(String text){
        for(Company company:companyRepository.findByCompanyNameStartsWithIgnoreCase(selectedCompany.getCompanyName())) {
            for (Car car : carRepository.findByCompany(company)) {
                for (Request request : requestRepository.findByCars(car)) {
                    requestVerifyService.setRequestCondition(request, false);
                }
            }
        }

    }

    public  void fillCarGridByCarModel (String text, Company selectedCompany){
        if(StringUtils.isEmpty(text)){
            carGrid.setContainerDataSource(new BeanItemContainer(Car.class,
                    carRepository.findByCompanyName(selectedCompany.getCompanyName())));
        }
        else {
            carGrid.setContainerDataSource(new BeanItemContainer(Car.class,
                    carRepository.findByCarModelStartsWithIgnoreCase(text)));
        }
    }

    public void fillCarGridBySelectedCompany(Company selectedCompany){
        if (selectedCompany == null){
            carGrid.setContainerDataSource(new BeanItemContainer(Car.class,
                    carRepository.findByCompany(selectedCompany)));
        }
        else {
            carGrid.setContainerDataSource(new BeanItemContainer(Car.class,
                    carRepository.findByCompany(selectedCompany)));
        }
    }

    public final void editCar(Car c){
        final boolean persisted = c.getId() != null;

        if (persisted){
            car = carRepository.findOne(c.getId());
        }
        else {
            car = c;
        }
        cancel.setVisible(persisted);

        BeanFieldGroup.bindFieldsUnbuffered(car,this);

        setVisible(true);

        save.focus();

        carModel.selectAll();
    }

    public void setChangeHandler(CarView.ChangeHandler h) {

        //fillPersonGrid(filterPerson.getValue(),selectedCompany);
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
}
