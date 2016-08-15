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
import factory_bd.repository.UserRoleRepository;
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
    private final UserRoleRepository roleRepository;
    private User user;
    private UserService userService;
    public final static String SESSION_USER_KEY = "SES_UKEY";
    public final static String VIEW_NAME = "";
    TextField email =new TextField("Email");
    PasswordField password =new PasswordField("Пароль");
    PasswordField newPassword =new PasswordField("Новый пароль (не менее 5 символов)");
    PasswordField confirmPassword =new PasswordField("Подтвердить пароль");
    Label askToChangePass = new Label("Необходимо сменить пароль");
    Label wrongPassOrLogin = new Label("Неверный пароль или логин");
    Label newPasswordIsTooSmall = new Label("Новый пароль слишком короткий");
    Button ok = new Button("Логин");
    Button change = new Button("Сохранить изменения");
   // Button cancel = new Button("Cancel");
    CssLayout actions = new CssLayout(ok,change);
   // CssLayout test = new CssLayout(email,password);
    @Autowired
    public LoginScreenView(UserRepository userRepository,UserRoleRepository userRoleRepository){
        this.userRepository = userRepository;
        this.roleRepository=userRoleRepository;
        userService = new UserService(userRepository,roleRepository);
        VerticalLayout loginScreenLayout = new VerticalLayout(wrongPassOrLogin,email,password,askToChangePass,newPasswordIsTooSmall,newPassword,confirmPassword,ok,change);
        addComponent(loginScreenLayout);
        setSpacing(true);
        loginScreenLayout.setSpacing(true);
        loginScreenLayout.setMargin(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
        change.setStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        ok.addClickListener(e -> loginUser());
        //change.addClickListener(e -> userService.changePassword(email.getValue(),password.getValue(),newPassword.getValue()));
        //cancel.addClickListener(e -> editCustomer(customer));
        setVisible(true);
        newPassword.setVisible(false);
        confirmPassword.setVisible(false);
        newPassword.setImmediate(true);
        newPassword.setMaxLength(15);
        change.setVisible(false);
        askToChangePass.setVisible(false);
        wrongPassOrLogin.setVisible(false);
        newPasswordIsTooSmall.setVisible(false);
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
        this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        if(user!=null){
            getUI().getNavigator().navigateTo(SpecialView.VIEW_NAME);
        }
    }

    public interface ChangeHandler {

        void onChange();
    }
    public final void loginUser(){
        wrongPassOrLogin.setVisible(false);
        if(!userService.haveUser(email.getValue())) {
            wrongPassOrLogin.setVisible(true);
            return;
        }
        {
            if(userService.checkPassword(email.getValue(),password.getValue())){
                if(userService.ifNeedToChangePassword(email.getValue())){
                    newPassword.setVisible(true);
                    confirmPassword.setVisible(true);
                    change.setVisible(true);
                    askToChangePass.setVisible(true);
                    ok.setVisible(false);
                    change.setClickShortcut(ShortcutAction.KeyCode.ENTER);
                    newPasswordIsTooSmall.setVisible(false);
                    change.addClickListener(e->{
                        //String checkPasswordLength;
                        newPassword.getValue().length();
                       // checkPasswordLength=newPassword.toString();
                        if(newPassword.getValue().length()<=4) {
                            Notification.show("Внимание!",
                                    "пароль должен быть не менее 5 символов!",
                                    Notification.Type.TRAY_NOTIFICATION.WARNING_MESSAGE);
                           // newPasswordIsTooSmall.setVisible(true);
                           // newPassword.setVisible(false);
                           // confirmPassword.setVisible(false);
                            //change.setVisible(false);
                            askToChangePass.setVisible(false);
                            //ok.setVisible(true);
                           // password.clear();
                            newPassword.clear();
                            confirmPassword.clear();
                            return;
                        }
                        userService.changePassword(email.getValue(),password.getValue(),newPassword.getValue(),confirmPassword.getValue());
                        newPassword.setVisible(false);
                        confirmPassword.setVisible(false);
                        change.setVisible(false);
                        askToChangePass.setVisible(false);
                        ok.setVisible(true);
                        newPasswordIsTooSmall.setVisible(false);
                        password.clear();
                    });

                    newPassword.clear();
                    confirmPassword.clear();
                }
                else {
                  //  getUI().getPage().setLocation("http://google.com");
                    user = userService.getValidUser(email.getValue(),password.getValue());
                    getUI().getSession().setAttribute(SESSION_USER_KEY, user);
                    //getUI().getNavigator().navigateTo(UserSettingsView.VIEW_NAME);
                    getUI().getNavigator().navigateTo(SpecialView.VIEW_NAME);
                }
            }
            else
                wrongPassOrLogin.setVisible(true);
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
