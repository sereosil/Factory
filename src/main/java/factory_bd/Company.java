package factory_bd;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


/**
 * Created by Валерий on 20.07.2016.
 */
@Entity
public class Company {

    @Id
    @GeneratedValue
    public Integer Id;

    private Person person;//ссылка на персону

    private Date additionDate;

    private Date endDate;

    private String companyName;
    protected Company(){}

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
}
