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
    @ManyToOne//(cascade = {CascadeType.ALL})
    private Person person;//FactoryUser

    private Date additionDate;

    private Date endDate;

    private String companyName;

    private String phoneNumber;

    private String companyAdress;

    public String getCompanyAdress() {
        return companyAdress;
    }

    public void setCompanyAdress(String companyAdress) {
        this.companyAdress = companyAdress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

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

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
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


    public Company(String companyName, String companyAdress, String phoneNumber)
    {
        this.companyName = companyName;
        this.companyAdress = companyAdress;
        this.phoneNumber = phoneNumber;
    }
    @Override
    public String toString() {
        return "Название: " + companyName + "; " +
                "Адрес: " + companyAdress + "; " +
                "Телефон: " + phoneNumber ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (person != null ? !person.equals(company.person) : company.person != null) return false;
        if (additionDate != null ? !additionDate.equals(company.additionDate) : company.additionDate != null)
            return false;
        if (endDate != null ? !endDate.equals(company.endDate) : company.endDate != null) return false;
        if (companyName != null ? !companyName.equals(company.companyName) : company.companyName != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(company.phoneNumber) : company.phoneNumber != null) return false;
        return companyAdress != null ? companyAdress.equals(company.companyAdress) : company.companyAdress == null;

    }

    @Override
    public int hashCode() {
        return 0;
    }
}
