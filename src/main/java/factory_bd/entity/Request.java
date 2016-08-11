package factory_bd.entity;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by sereo_000 on 20.07.2016.
 */
@Entity
public class Request {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Person> persons; // fAnnotation

    //@OneToMany(fetch = FetchType.EAGER)
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Car> cars; //JPA Annotation

    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    private User createdBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private User approvedBy;

    private boolean accepted;

    @Enumerated
    private RequestState requestState = RequestState.undefined;
    private String description;
    private Date dateFrom;
    private Date dateTo;

    public Request(){}


    public Request(Date dateFrom, Date dateTo, User createdBy) {
        //this.company = company;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.createdBy = createdBy;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Request(Company company) {
        this.company = company;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RequestState getRequestState() {
        return requestState;
    }

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }

    public Integer getId() {
        return id;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "Запрос:" +
                "id=" + id +
                ", компания=" + company +
                ", создан=" + createdBy +
                ", подтвержден=" + approvedBy +
                ", описание='" + description + '\'' +
                ", начало=" + dateFrom +
                ", конец=" + dateTo + ", acepted=" + accepted;
    }
}
