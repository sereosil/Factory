package factory_bd;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    private String pasportIdentificator;

    private String companyName;

    public String getCompanyName() {
        return companyName;
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

    public String getPasportIdentificator() {
        return pasportIdentificator;
    }

    public void setPasportIdentificator(String pasportIdentificator) {
        this.pasportIdentificator = pasportIdentificator;
    }

    public Integer getId() {
        return id;
    }

    public Person(String firstName, String companyName, String lastName, String pasportIdentificator) {
        this.firstName = firstName;
        this.companyName = companyName;
        this.lastName = lastName;
        this.pasportIdentificator = pasportIdentificator;
    }

    @Override
    public String toString() {
        return "Person{" +
                "companyName='" + companyName + '\'' +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pasportIdentificator='" + pasportIdentificator + '\'' +
                '}';
    }
}
