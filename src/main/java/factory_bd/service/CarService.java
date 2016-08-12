package factory_bd.service;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ListSelect;
import factory_bd.entity.Car;
import factory_bd.entity.Company;
import factory_bd.entity.Person;
import factory_bd.repository.CarRepository;
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

    @Deprecated
    public boolean doRepositoryHaveCar(String carRegistrationNumber){
        for (Car car:carRepository.findByCarModelStartsWithIgnoreCase(carRegistrationNumber)){
            return true;
        }
        return false;
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
}
