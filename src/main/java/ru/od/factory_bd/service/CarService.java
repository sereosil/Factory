package ru.od.factory_bd.service;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ListSelect;
import ru.od.factory_bd.entity.Car;
import ru.od.factory_bd.entity.Company;
import ru.od.factory_bd.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Валерий on 28.07.2016.
 */
@SpringComponent
public class CarService {
    private CarRepository carRepository;

    private Car car;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }


    public void addCar(Car car){
        carRepository.save(car);
    }
    public void deleteCar(Car car){
        carRepository.delete(car);
    }

    public void changeCarColor(Car car,String color){
        car.setCarColor(color);
        carRepository.save(car);
    }
    public void changeCarRegistrationNumber(Car car, String regNubmer){
        car.setCarRegistrationNumber(regNubmer);
        carRepository.save(car);
    }

    public void fillCarListInRequest(ListSelect listSelect, Company company){
        listSelect.removeAllItems();
        for (Car car : carRepository.findByCompany(company)) {
            listSelect.addItem(car);
        }

    }
    public void fillCarGrid(Grid grid, Company company)
    {
        grid.setContainerDataSource( new BeanItemContainer(Car.class,carRepository.findByCompany(company)));
    }

    public boolean doRepositoryHaveCar(Car car){
        boolean check = false;
        for (Car searchedCar:carRepository.findAll()){
            if( car.getCarRegistrationNumber().equals(searchedCar.getCarRegistrationNumber())){
                check = true;
                break;
            }
            else {
                check = false;
            }
        }
        return check;
    }
}
