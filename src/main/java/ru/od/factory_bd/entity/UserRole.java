package ru.od.factory_bd.entity;

import javax.persistence.*;

/**
 * Created by sereo_000 on 20.07.2016.
 */
@Entity@Table(name = "admit_user_role")
public class UserRole {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "can_view")
    private boolean view=false;

    @Column(name = "can_add")
    private boolean add=false;

    @Column(name = "can_confirm")
    private boolean confirm=false;

    @Column(name = "can_admin")
    private boolean admin=false;

    public UserRole(boolean admin, boolean view, boolean add, boolean confirm) {
        this.admin = admin;
        this.view = view;
        this.add = add;
        this.confirm = confirm;
    }

    protected UserRole(){
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
