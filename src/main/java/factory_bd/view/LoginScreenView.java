package factory_bd.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
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
@SpringView (name =LoginScreenView.VIEW_NAME)
@SpringComponent
@UIScope
public class LoginScreenView extends VerticalLayout implements View{
    private final UserRepository userRepository;
    private User user;
    private UserService userService;
    public final static String SESSION_USER_KEY = "SES_UKEY";
    public final static String VIEW_NAME = "";
    TextField email =new TextField("Email");
    PasswordField password =new PasswordField("Password");
    PasswordField newPassword =new PasswordField("New password");
    PasswordField confirmPassword =new PasswordField("Confirm password");
    Button ok = new Button("Login");
    Button change = new Button("Set changes");
   // Button cancel = new Button("Cancel");
    CssLayout actions = new CssLayout(ok,change);
   // CssLayout test = new CssLayout(email,password);
    @Autowired
    public LoginScreenView(UserRepository userRepository){
        this.userRepository = userRepository;
        userService = new UserService(userRepository);
        VerticalLayout loginScreenLayout = new VerticalLayout(email,password,newPassword,confirmPassword,ok,change);
        addComponent(loginScreenLayout);
        setSpacing(true);
        loginScreenLayout.setSpacing(true);
        loginScreenLayout.setMargin(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        ok.addClickListener(e -> loginUser());
        //change.addClickListener(e -> userService.changePassword(email.getValue(),password.getValue(),newPassword.getValue()));
        //cancel.addClickListener(e -> editCustomer(customer));
        setVisible(true);
        newPassword.setVisible(false);
        confirmPassword.setVisible(false);
        change.setVisible(false);
        setSizeFull();
    }
    //@Override
    public void init(){
        VerticalLayout loginScreenLayout = new VerticalLayout(email,password,newPassword,confirmPassword,ok,change);
        loginScreenLayout.setSpacing(true);
        loginScreenLayout.setMargin(true);
        removeAllComponents();
        addComponent(loginScreenLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
       // new Navigator(this, this);
      // getUI().getNavigator().addView(UserSettingsView.NAME, UserSettingsView.class);
    }

    public interface ChangeHandler {

        void onChange();
    }
    public final void loginUser(){
        if(!userService.haveUser(email.getValue()))
        return;

        {
            if(userService.checkPassword(email.getValue(),password.getValue())){
                if(userService.ifNeedToChangePassword(email.getValue())){
                    newPassword.setVisible(true);
                    confirmPassword.setVisible(true);
                    change.setVisible(true);
                    change.addClickListener(e->userService.changePassword(email.getValue(),password.getValue(),newPassword.getValue()));
                }
                else {
                  //  getUI().getPage().setLocation("http://google.com");
                    user = userService.getValidUser(email.getValue(),password.getValue());
                    getUI().getSession().setAttribute(SESSION_USER_KEY, user);
                    getUI().getNavigator().navigateTo(UserSettingsView.VIEW_NAME);
                }
            }
        }
        //BeanFieldGroup.bindFieldsUnbuffered(userRepository,this);
        setVisible(true);

    }
    public void setChangeHandler(ChangeHandler h){
        // ChangeHandler is notified when either save or delete
        // is clicked
        ok.addClickListener(e -> h.onChange());
        change.addClickListener(e->h.onChange());
    }
}
