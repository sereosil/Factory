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
    TextField firstName = new TextField("Имя");
    TextField lastName = new TextField("Фамилия");
    TextField email = new TextField("Email");
    TextField contact = new TextField("Телефон");
    CheckBox view = new CheckBox("Просмотр");
    CheckBox add = new CheckBox("Добавление");
    CheckBox confirm = new CheckBox("Подтверждение");
    CheckBox admin = new CheckBox("Администратор");
    Label annotation = new Label("Сменить пароль");
    Label wrongPass = new Label("Вы ввели неверный пароль или новый пароль не совпадает с подтверждаемым");
    PasswordField oldPassword =new PasswordField("Старый пароль");
    PasswordField newPassword =new PasswordField("Новый пароль");
    PasswordField confirmPassword =new PasswordField("Подтвердить пароль");
    Label newPasswordIsTooSmall = new Label("Новый пароль слишком короткий");
    Label newPasswordWasSet = new Label("Новый пароль сохранен!");
    Button ok = new Button("Применить");
    Button back = new Button("Назад");
    // Button cancel = new Button("Cancel");


    @Autowired
    public UserSettingsView(UserRepository userRepository, UserRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        userService = new UserService(userRepository,roleRepository);
        setSpacing(true);
        setMargin(true);
        setVisible(true);

    }
  /*  public UserSettingsView() {
        this.userRepository = null;
        this.roleRepository = null;
        userService = new UserService(userRepository);
        VerticalLayout userRoleLayout = new VerticalLayout(view,add,confirm,admin);
        HorizontalLayout layout = new HorizontalLayout(firstName,lastName,email,contact,userRoleLayout,oldPassword,newPassword,confirmPassword,actions);
        addComponents(layout);
        setSpacing(true);

    }
*/
  public void setUser(User user) {
      this.user = user;
  }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        init();
    }
    public void init(){
        firstName.setValue(user.getFirstName());
        lastName.setValue(user.getLastName());
        email.setValue(user.getEmail());
        contact.setValue(user.getContact());
        view.setValue(user.getUserRole().isView());
        add.setValue(user.getUserRole().isAdd());
        confirm.setValue(user.getUserRole().isConfirm());
        admin.setValue(user.getUserRole().isAdmin());
        HorizontalLayout actions = new HorizontalLayout(ok,back);
        actions.setMargin(true);
        actions.setSpacing(true);
        HorizontalLayout userRoleLayout = new HorizontalLayout(view,add,confirm,admin);
        userRoleLayout.setMargin(true);
        userRoleLayout.setSpacing(true);
        VerticalLayout layout = new VerticalLayout(firstName,lastName,email,contact,userRoleLayout,annotation,wrongPass,newPasswordIsTooSmall,newPasswordWasSet,oldPassword,newPassword,confirmPassword,actions);
        addComponents(layout);
        setSpacing(true);
        setMargin(true);
        newPasswordIsTooSmall.setVisible(false);
        newPasswordWasSet.setVisible(false);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        ok.addClickListener(e -> {
            final Window window = new Window("");
            window.setWidth(300.0f, Unit.PIXELS);
            window.setPosition(400,150);
            Button ok = new Button("Да");
            Button no = new Button("Нет");
            HorizontalLayout buttons = new HorizontalLayout(ok,no);
            buttons.setSpacing(true);
            Label areSure = new Label("Сохранить?");
            final FormLayout content = new FormLayout(areSure,buttons);

            window.setContent(content);
            UI.getCurrent().addWindow(window);
            ok.addClickListener(u->{
                applyChanges(user);
                window.close();
            });
            no.addClickListener(u->{
                window.close();
            });

        });
        back.addClickListener(e->getUI().getNavigator().navigateTo(SpecialView.VIEW_NAME));
        add.setReadOnly(true);
        view.setReadOnly(true);
        confirm.setReadOnly(true);
        admin.setReadOnly(true);
        wrongPass.setVisible(false);
        //cancel.addClickListener(e -> editCustomer(customer));
        setVisible(true);
    }

    public interface ChangeHandler {

        void onChange();
    }
    public final void applyChanges(User user){
        newPasswordIsTooSmall.setVisible(false);
        userService.changeUserEmail(user,email.getValue());
        userService.changeUserFirstName(user,firstName.getValue());
        userService.changeUserLastName(user,lastName.getValue());
        userService.changeUserPhone(user,contact.getValue());
        //String checkPasswordLength;
        //checkPasswordLength=newPassword.toString();
        if(!newPassword.getValue().isEmpty()){
            if(newPassword.getValue().length()<=4){
                newPasswordIsTooSmall.setVisible(true);
                oldPassword.clear();
                newPassword.clear();
                confirmPassword.clear();
                return;
            }
            userService.changePassword(email.getValue(),oldPassword.getValue(),newPassword.getValue(),confirmPassword.getValue());
            newPasswordIsTooSmall.setVisible(false);
            newPasswordWasSet.setVisible(true);
            oldPassword.clear();
            newPassword.clear();
            confirmPassword.clear();
        }
        newPasswordIsTooSmall.setVisible(false);
        //BeanFieldGroup.bindFieldsUnbuffered(user,this);
        setVisible(true);
    }
    public void setChangeHandler(UserSettingsView.ChangeHandler h){
        // ChangeHandler is notified when either save or delete
        // is clicked
        ok.addClickListener(e -> h.onChange());
    }
}
