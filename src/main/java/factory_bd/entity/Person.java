package factory_bd.entity;

import javax.persistence.*;

/**
 * Created by Валерий on 20.07.2016.
 */
@Entity
public class Person {
    @Id
    @GeneratedValue

    private Integer id;

    private String firstName;

    private String lastName;

    private String passportIdentification;

    @ManyToOne//(cascade = {CascadeType.ALL})
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    private String companyName;

    public String getCompanyName() {
        return company.getCompanyName();
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassportIdentification() {
        return passportIdentification;
    }

    public void setPassportIdentification(String passportIdentification) {
        this.passportIdentification = passportIdentification;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    Person() {

    }

    public Person(String firstName, String lastName, Company company, String passportIdentification) {
        this.firstName = firstName;
        this.lastName = lastName;
       // this.companyName = companyName;
        this.company = company;
        this.passportIdentification = passportIdentification;
    }

    @Override
    public String toString() {
        return firstName + ", " +
                lastName + ", " +
                "№ паспорта: " + passportIdentification + "\n";
    }
}
