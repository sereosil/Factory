package factory_bd.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import factory_bd.entity.User;
import factory_bd.entity.UserRole;
import factory_bd.repository.UserRepository;
import factory_bd.repository.UserRoleRepository;
import factory_bd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import static factory_bd.view.LoginScreenView.SESSION_USER_KEY;

/**
 * Created by sereo_000 on 27.07.2016.
 */
@SpringView(name = UserSettingsView.VIEW_NAME)
@SpringComponent
@UIScope
public class UserSettingsView extends VerticalLayout implements View {
    public static final String VIEW_NAME ="USER_SETTINGS" ;
    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private User user;
    private UserRole userRole;
    private UserService userService;
    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    TextField email = new TextField("Email");
    TextField contact = new TextField("Phone number");
    CheckBox view = new CheckBox("View");
    CheckBox add = new CheckBox("Add");
    CheckBox confirm = new CheckBox("Confirm");
    CheckBox admin = new CheckBox("Admin");
    PasswordField oldPassword =new PasswordField("Old Password");
    PasswordField newPassword =new PasswordField("New Password");
    PasswordField confirmPassword =new PasswordField("Confirm Password");
    Button ok = new Button("Apply");
    // Button cancel = new Button("Cancel");
    CssLayout actions = new CssLayout(ok);
    @Autowired

    public UserSettingsView(UserRepository userRepository, UserRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        userService = new UserService(userRepository);
        VerticalLayout userRoleLayout = new VerticalLayout(view,add,confirm,admin);
        HorizontalLayout layout = new HorizontalLayout(firstName,lastName,email,contact,userRoleLayout,oldPassword,newPassword,confirmPassword,actions);
        addComponents(layout);
        setSpacing(true);

    }
    public UserSettingsView() {
        this.userRepository = null;
        this.roleRepository = null;
        userService = new UserService(userRepository);
        VerticalLayout userRoleLayout = new VerticalLayout(view,add,confirm,admin);
        HorizontalLayout layout = new HorizontalLayout(firstName,lastName,email,contact,userRoleLayout,oldPassword,newPassword,confirmPassword,actions);
        addComponents(layout);
        setSpacing(true);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
    }
    public void init(){
        VerticalLayout userRoleLayout = new VerticalLayout(view,add,confirm,admin);
        HorizontalLayout layout = new HorizontalLayout(firstName,lastName,email,contact,userRoleLayout,oldPassword,newPassword,confirmPassword,actions);
        addComponents(layout);
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        ok.addClickListener(e -> applyChanges(user));
        add.setReadOnly(true);
        view.setReadOnly(true);
        confirm.setReadOnly(true);
        admin.setReadOnly(true);
        //cancel.addClickListener(e -> editCustomer(customer));
        setVisible(false);
    }

    public interface ChangeHandler {

        void onChange();
    }
    public final void applyChanges(User user){
        userService.changeUserEmail(user,email.getValue());
        userService.changePassword(email.getValue(),oldPassword.getValue(),newPassword.getValue());
        userService.changeUserFirstName(user,firstName.getValue());
        userService.changeUserLastName(user,lastName.getValue());
        userService.changeUserPhone(user,contact.getValue());
        BeanFieldGroup.bindFieldsUnbuffered(user,this);
        setVisible(true);
    }
    public void setChangeHandler(UserSettingsView.ChangeHandler h){
        // ChangeHandler is notified when either save or delete
        // is clicked
        ok.addClickListener(e -> h.onChange());
    }
}
