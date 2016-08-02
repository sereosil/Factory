package factory_bd.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import factory_bd.entity.Car;
import factory_bd.entity.Company;
import factory_bd.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Валерий on 01.08.2016.
 */
public class CarView extends VerticalLayout implements View{

    private final CarRepository carRepository;
    private Car car;
    private Company selectedCompany;

    TextField carModel = new TextField("Car model");
    TextField carColor = new TextField("Color of car");
    TextField carRegistrationNumber = new TextField("Registration");
    TextField filterCar;

    Label searchLabel;

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    Button addNewCarButton;

    Grid carGrid;


    @Autowired
    public CarView(CarRepository carRepository) {
        this.carRepository = carRepository;
        this.addNewCarButton = new Button("New car", FontAwesome.PLUS);
        this.filterCar = new TextField();
        this.carGrid = new Grid();
        this.searchLabel = new Label("Search:");
    }

    public void init(){
        carGrid.setHeight(300,Unit.PIXELS);
        carGrid.setColumns("id","carModel","carColor","carRegistrationNumber");


    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
