package factory_bd.entity;

/**
 * Created by Валерий on 20.07.2016.
 */

import javax.persistence.*;

@Entity
public class Car {
    @Id
    @GeneratedValue
    private Integer id;

    private String carModel;

    private String carColor;

    private String carRegistrationNumber;

    @ManyToOne//(cascade = {CascadeType.ALL})
    private Company company;

    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
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

    private Car() {

    }
    public Car(String carColor, String carRegistrationNumber, String carModel, Company company) {
        this.carColor = carColor;
        this.carRegistrationNumber = carRegistrationNumber;
        this.carModel = carModel;
        this.company = company;
        //this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Car reg. №: " + carRegistrationNumber + "; " +
                "Model: " + carModel + "; " +
                "Color: " + carColor + ".";
    }
}
