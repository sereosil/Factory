package factory_bd.entity;

/**
 * Created by Валерий on 20.07.2016.
 */

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Car {
    @Id
    @GeneratedValue
    private Integer id;

    private String carModel;

    private String carColor;

    private String carRegistrationNumber;

    public String getCarRegistrationNumber() {
        return carRegistrationNumber;
    }

    public void setCarRegistrationNumber(String carRegistrationNumber) {
        this.carRegistrationNumber = carRegistrationNumber;
    }
    public Integer getId() {
        return id;
    }
    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }
    public Car()
    {

    }
    public Car(String carColor, String carRegistrationNumber, String carModel) {
        this.carColor = carColor;
        this.carRegistrationNumber = carRegistrationNumber;
        this.carModel = carModel;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", carRegistrationNumber='" + carRegistrationNumber + '\'' +
                ", carModel='" + carModel + '\'' +
                ", carColor='" + carColor + '\'' +
                '}';
    }
}
