package factory_bd.entity;


import javax.persistence.*;
import java.util.Date;


/**
 * Created by Валерий on 20.07.2016.
 */
@Entity
public class Company {

    @Id
    @GeneratedValue
    private Integer Id;

    //By specifying the above options you tell hibernate to save them to the database when saving their parent.
    @ManyToOne(cascade = {CascadeType.ALL})
    private Person person;//FactoryUser

    private Date additionDate;

    private Date endDate;

    private String companyName;

    public Date getAdditionDate() {
        return additionDate;
    }

    public Person getPerson() {
        return person;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setAdditionDate(Date additionDate) {
        this.additionDate = additionDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    protected Company(){

    }

    public Company(String companyName) {
        this.companyName = companyName;
    }

    protected Company(String companyName, Person person/*, Date additionDate, Date endDate*/) {
        this.companyName = companyName;
        this.person = person;
        /*this.additionDate = additionDate;
        this.endDate = endDate;*/
    }

    @Override
    public String toString() {
        return "Company{" +
                "Id=" + Id +
                ", companyName='" + companyName + '\'' +
                ", person=" + person +
                ", additionDate=" + additionDate +
                ", endDate=" + endDate +
                '}';
    }
}
