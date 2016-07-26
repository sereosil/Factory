package factory_bd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by sereo_000 on 20.07.2016.
 */
@Entity
public class UserRole {
    @Id
    @GeneratedValue
    private Integer id;
    private boolean view=false;
    private boolean add=false;
    private boolean confirm=false;
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
