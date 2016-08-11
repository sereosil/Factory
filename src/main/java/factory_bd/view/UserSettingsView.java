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
    Label wrongPass = new Label("You typed a wrong password or new password didn't match with conform password");
    PasswordField oldPassword =new PasswordField("Старый пароль");
    PasswordField newPassword =new PasswordField("Новый пароль");
    PasswordField confirmPassword =new PasswordField("Подтвердить пароль");
    Button ok = new Button("Применить");
    // Button cancel = new Button("Cancel");
    CssLayout actions = new CssLayout(ok);

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
        HorizontalLayout userRoleLayout = new HorizontalLayout(view,add,confirm,admin);
        userRoleLayout.setMargin(true);
        userRoleLayout.setSpacing(true);
        VerticalLayout layout = new VerticalLayout(firstName,lastName,email,contact,userRoleLayout,annotation,wrongPass,oldPassword,newPassword,confirmPassword,actions);
        addComponents(layout);
        setSpacing(true);
        setMargin(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        ok.addClickListener(e -> applyChanges(user));
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
        userService.changeUserEmail(user,email.getValue());
        userService.changeUserFirstName(user,firstName.getValue());
        userService.changeUserLastName(user,lastName.getValue());
        userService.changeUserPhone(user,contact.getValue());
        userService.changePassword(email.getValue(),oldPassword.getValue(),newPassword.getValue(),confirmPassword.getValue());

        getUI().getNavigator().navigateTo(AdminWindowView.VIEW_NAME);
        //BeanFieldGroup.bindFieldsUnbuffered(user,this);
        setVisible(true);
    }
    public void setChangeHandler(UserSettingsView.ChangeHandler h){
        // ChangeHandler is notified when either save or delete
        // is clicked
        ok.addClickListener(e -> h.onChange());
    }
}
