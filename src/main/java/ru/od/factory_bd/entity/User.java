package ru.od.factory_bd.entity;

import javax.persistence.*;

/**
 * Created by sereo_000 on 20.07.2016.
 */
@Entity@Table(name = "admit_user")
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    private String contact;
    @ManyToOne(fetch = FetchType.EAGER)
    private UserRole userRole;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private boolean needToChangePassword=false;
    protected User(){
    }

    public User(String firstName, String lastName, String contact,UserRole role, String email,String passwordHash) {
        this.email=email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.userRole=role;
        this.passwordHash=passwordHash;
    }

    public User(String lastName, String firstName, String email, String passwordHash) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public boolean isNeedToChangePassword() {
        return needToChangePassword;
    }

    public void setNeedToChangePassword(boolean needToChangePassword) {
        this.needToChangePassword = needToChangePassword;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {

        return firstName;
    }

    public UserRole getUserRole() {

        return userRole;
    }

    public String getContact() {

        return contact;
    }

    public Integer getId() {

        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setContact(String contact) {

        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return
                "Имя: '" + firstName + '\'' +
                        ", Фамилия: '" + lastName + '\'' +
                        ", Телефон: '" + contact + '\'';
    }
}
