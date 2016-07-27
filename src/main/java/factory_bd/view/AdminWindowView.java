package factory_bd.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import factory_bd.entity.User;
import factory_bd.entity.UserRole;
import factory_bd.repository.UserRepository;
import factory_bd.repository.UserRoleRepository;
import factory_bd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by sereo_000 on 27.07.2016.
 */
@SpringComponent
@UIScope
public class AdminWindowView extends VerticalLayout {
    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private User user;
    private UserRole userRole;
    private UserService userService;
    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    CheckBox view = new CheckBox("View");
    CheckBox add = new CheckBox("Add");
    CheckBox confirm = new CheckBox("Confirm");
    CheckBox admin = new CheckBox("Admin");
    // Кнопки
    Button save = new Button("Save", FontAwesome.SAVE);
   // Button cancel = new Button("Cancel");
    Button delete = new Button("Delete",FontAwesome.TRASH_O);
   // Button addUser = new Button("Add User",FontAwesome.PLUS);
    CssLayout actions = new CssLayout(save,delete);
    @Autowired
    public AdminWindowView(UserRepository userRepository, UserRoleRepository roleRepository){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        userService=new UserService(userRepository);
        addComponents(firstName, lastName, view, add, confirm, admin, actions);
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addClickListener(e -> editUser(user));
        delete.addClickListener(e -> userRepository.delete(user));
       // addUser.addClickListener(e -> addNewUser());
        //cancel.addClickListener(e -> editCustomer(customer));
        setVisible(false);
    }
    public interface ChangeHandler {

        void onChange();
    }
    public final void editUser(User user){
        UserRole userRole= new UserRole(admin.getValue(), view.getValue(),add.getValue(),confirm.getValue());
        userService.changeUserRole(user,userRole);
        BeanFieldGroup.bindFieldsUnbuffered(user,this);
        setVisible(true);
    }
    public void setChangeHandler(ChangeHandler h){
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
    //ругается потому чтотвои репозитории не инициализированны - делается в конструкторе

}
