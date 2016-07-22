package factory_bd;
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

    @ManyToOne(cascade = {CascadeType.ALL})
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

    protected Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    Person() {

    }

    public Person(String firstName, String lastName, Company companyName, String passportIdentification) {
        this.firstName = firstName;
        this.lastName = lastName;
        //this.company = companyName;
        this.passportIdentification = passportIdentification;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                /*", company='" + company + '\'' +*/
                ", passportIdentification='" + passportIdentification + '\'' +
                '}';
    }
}
