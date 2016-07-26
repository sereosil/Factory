package factory_bd.view;

import factory_bd.entity.User;
import factory_bd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by sereo_000 on 25.07.2016.
 */
@SpringComponent
@UIScope
public class LoginScreen extends VerticalLayout{
    private final UserRepository userRepository;
    private User user;
    TextField email =new TextField("Email");
    TextField password =new TextField("Password");
    TextField newPassword =new TextField("New password");
    TextField confirmPassword =new TextField("Confirm password");
    Button ok = new Button("Login");
    Button cancel = new Button("Cancel");
    CssLayout actions = new CssLayout(ok,cancel);
    @Autowired
    public LoginScreen(UserRepository userRepository){
        this.userRepository = userRepository;
        addComponents();
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        //save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        //save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }
}
