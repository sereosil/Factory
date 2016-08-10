package factory_bd.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.Renderer;
import com.vaadin.ui.themes.ValoTheme;
import factory_bd.entity.Company;
import factory_bd.entity.User;
import factory_bd.entity.UserRole;
import factory_bd.repository.UserRepository;
import factory_bd.repository.UserRoleRepository;
import factory_bd.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import static factory_bd.view.LoginScreenView.SESSION_USER_KEY;

/**
 * Created by sereo_000 on 27.07.2016.
 */
@SpringView(name = AdminWindowView.VIEW_NAME)
@SpringComponent
@UIScope
public class AdminWindowView extends VerticalLayout implements View {
    public static final String VIEW_NAME ="ADMIN_SETTINGS" ;
    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private User user;
    private User userNew;
    private UserRole userRole;
    private UserService userService;
    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final TextField email = new TextField("Email");
    private final PasswordField password = new PasswordField("Password");
    private final TextField contact = new TextField("Contact");
    private final CheckBox view = new CheckBox("View");
    private final CheckBox add = new CheckBox("Add");
    private final CheckBox confirm = new CheckBox("Confirm");
    private final CheckBox admin = new CheckBox("Admin");
    //private final CheckBox changePasswordCheck = new CheckBox("Need to change password?");
    private final Grid grid = new Grid();
    private final Button addNewBtn;
    private final TextField filter;
    // Кнопки
    Button save = new Button("Save", FontAwesome.SAVE);
    Button addBtn = new Button("Save", FontAwesome.PLUS);
   // Button cancel = new Button("Cancel");
    Button delete = new Button("Delete",FontAwesome.TRASH_O);
   // Button addUser = new Button("Add User",FontAwesome.PLUS);
    CssLayout buttons = new CssLayout(save,delete);
    @Autowired
    public AdminWindowView(UserRepository userRepository, UserRoleRepository roleRepository){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.addNewBtn = new Button("New user", FontAwesome.PLUS);
        this.filter = new TextField();
        userService=new UserService(userRepository,roleRepository);
        setVisible(true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        init();
    }
    public void init() {
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        HorizontalLayout userRoleChange = new HorizontalLayout(view,add,confirm,admin,buttons);
        VerticalLayout changeUser = new VerticalLayout(firstName,lastName,contact,userRoleChange,email,password);
        addComponents(actions,grid,changeUser,userRoleChange);
        setSpacing(true);
        actions.setSpacing(true);
        actions.setMargin(true);
        changeUser.setMargin(true);
        userRoleChange.setMargin(true);
        changeUser.setSpacing(true);
        userRoleChange.setSpacing(true);
        changeUser.setVisible(false);
        userRoleChange.setVisible(false);
        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "firstName", "lastName");
        buttons.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addClickListener(e -> {
            UserRole userRole = new UserRole(admin.getValue(), view.getValue(),add.getValue(),confirm.getValue());
            userService.setRole(userRole);
            userNew.setUserRole(userRole);
            if(!password.getValue().isEmpty()) {
                userNew.setFirstName(firstName.getValue());
                userNew.setLastName(lastName.getValue());
                userNew.setContact(contact.getValue());
                userNew.setEmail(email.getValue());
                userNew.setPasswordHash(DigestUtils.md5Hex(password.getValue()));
            }
            userService.addUser(userNew);

            listUsers(null);
        });
       // addBtn.addClickListener(e -> editUser(new User("","")));
        delete.addClickListener(e -> {
            userService.deleteUser(userNew);
            listUsers(null);
        });
        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
               changeUser.setVisible(false);

            } else {
                changeUser.setVisible(true);
                userRoleChange.setVisible(true);

                //changeUser.setVisible(true);
                this.editUser((User) grid.getSelectedRow());
            }
        });
        filter.setInputPrompt("Filter by last name");
        filter.addTextChangeListener( e-> listUsers(e.getText()));
        addNewBtn.addClickListener(e -> {
            userRoleChange.setVisible(true);
            changeUser.setVisible(true);
            editUser(new User("","","",userRole,"",""));
        });

        listUsers(null);
    }

    /*public final void addUser() {

        UserRole userRole = new UserRole(admin.getValue(), view.getValue(),add.getValue(),confirm.getValue());
        userService.setRole(userRole);
        userService.addUser(firstName.getValue(),lastName.getValue(),contact.getValue(),userRole,email.getValue(),password.getValue());
       // BeanFieldGroup.bindFieldsUnbuffered(user,this);
        setVisible(true);
    }*/

    public interface ChangeHandler {

        void onChange();
    }
    public final void editUser(User user){
        final boolean persisted = user.getId() != null;

        if (persisted){
            userNew = userRepository.findOne(user.getId());
            admin.setValue(userNew.getUserRole().isAdmin());
            add.setValue(userNew.getUserRole().isAdd());
            confirm.setValue(userNew.getUserRole().isConfirm());
            view.setValue(userNew.getUserRole().isView());
            //userService.changeUserRole(userNew,userRole);
            //userService.addUser(firstName.getValue(),lastName.getValue(),contact.getValue(),userRole,email.getValue(),password.getValue());

        }
        else {
            userNew = user;
            /*UserRole userRole = new UserRole(admin.getValue(), view.getValue(),add.getValue(),confirm.getValue());
            userService.setRole(userRole);
            userNew.setUserRole(userRole);*/
        }
        //save.setVisible(persisted);

      //  BeanFieldGroup.bindFieldsUnbuffered(userNew,this);

        setVisible(true);

        save.focus();

        lastName.selectAll();
    }
    public void setChangeHandler(ChangeHandler h){
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
    private void listUsers(String text) {
        if (StringUtils.isEmpty(text)) {
            grid.setContainerDataSource(
                    new BeanItemContainer(User.class, userRepository.findAll()));
        } else {
            grid.setContainerDataSource(new BeanItemContainer(User.class,
                    userRepository.findByLastNameStartsWithIgnoreCase(text)));
        }
    }
    //ругается потому чтотвои репозитории не инициализированны - делается в конструкторе

}
