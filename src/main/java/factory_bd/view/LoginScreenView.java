package factory_bd.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import factory_bd.entity.User;
import factory_bd.repository.UserRepository;
import factory_bd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by sereo_000 on 25.07.2016.
 */
@SpringComponent
@UIScope
public class LoginScreenView extends VerticalLayout{
    private final UserRepository userRepository;
    private User user;
    private UserService userService;
    TextField email =new TextField("Email");
    PasswordField password =new PasswordField("Password");
    TextField newPassword =new TextField("New password");
    TextField confirmPassword =new TextField("Confirm password");
    Button ok = new Button("Login");
   // Button cancel = new Button("Cancel");
    CssLayout actions = new CssLayout(ok);
    @Autowired
    public LoginScreenView(UserRepository userRepository){
        this.userRepository = userRepository;
        userService = new UserService(userRepository);
        addComponents(email,password,newPassword,confirmPassword,actions);
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        ok.addClickListener(e -> LoginUser(user));
        //cancel.addClickListener(e -> editCustomer(customer));
        setVisible(false);
        newPassword.setVisible(false);
        confirmPassword.setVisible(false);
    }
    public interface ChangeHandler {

        void onChange();
    }
    public final void LoginUser(User user){
        final boolean persisted =user.getId() != null;
        if(userService.haveUser(email.getValue())){
            if(userService.checkPassword(email.getValue(),password.getValue())){
                if(userService.ifDefaultPassword(email.getValue(),password.getValue())){
                    newPassword.setVisible(true);
                    confirmPassword.setVisible(true);
                    userService.changePassword(email.getValue(),password.getValue(),newPassword.getValue());
                    //редирект
                }
            }
        }
        BeanFieldGroup.bindFieldsUnbuffered(user,this);
        setVisible(true);
    }
    public void setChangeHandler(ChangeHandler h){
        // ChangeHandler is notified when either save or delete
        // is clicked
        ok.addClickListener(e -> h.onChange());
    }
}
