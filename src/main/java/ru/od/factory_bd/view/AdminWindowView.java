package ru.od.factory_bd.view;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.od.factory_bd.entity.User;
import ru.od.factory_bd.entity.UserRole;
import ru.od.factory_bd.repository.UserRepository;
import ru.od.factory_bd.repository.UserRoleRepository;
import ru.od.factory_bd.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by sereo_000 on 27.07.2016.
 */
@SpringView(name = AdminWindowView.VIEW_NAME)//ВЕЗДЕ!!!!!!!
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
    private final TextField firstName = new TextField("Имя");
    private final TextField lastName = new TextField("Фамилия");
    private final TextField email = new TextField("E-mail");
    private final PasswordField password = new PasswordField("Пароль");
    private final TextField contact = new TextField("Телефон");
    private final CheckBox view = new CheckBox("Просмотр");
    private final CheckBox add = new CheckBox("Добавление");
    private final CheckBox confirm = new CheckBox("Подтверждение");
    private final CheckBox admin = new CheckBox("Администратор");
    Label newPasswordIsTooSmall = new Label("Пароль слишком короткий");
    //private final CheckBox changePasswordCheck = new CheckBox("Need to change password?");
    private final Grid grid = new Grid();
    private final Button addNewBtn;
    private final TextField filter;
    // Кнопки
    Button save = new Button("Сохранить", FontAwesome.SAVE);
    Button addBtn = new Button("Save", FontAwesome.PLUS);
   // Button cancel = new Button("Cancel");
    Button delete = new Button("Удалить",FontAwesome.TRASH_O);
   // Button addUser = new Button("Add User",FontAwesome.PLUS);
    CssLayout buttons = new CssLayout(save,delete);
    @Autowired
    public AdminWindowView(UserRepository userRepository, UserRoleRepository roleRepository){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.addNewBtn = new Button("Добавить пользователя", FontAwesome.PLUS);
        this.filter = new TextField();
        userService=new UserService(userRepository,roleRepository);
        setVisible(true);
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        init();
    }
    public void init() {
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        HorizontalLayout userRoleChange = new HorizontalLayout(view,add,confirm,admin,buttons);
        VerticalLayout changeUser = new VerticalLayout(firstName,lastName,contact,userRoleChange,email,newPasswordIsTooSmall,password);
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
        newPasswordIsTooSmall.setVisible(false);
        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "firstName", "lastName");
        grid.getColumn("id").setHeaderCaption("ID");
        grid.getColumn("firstName").setHeaderCaption("Имя");
        grid.getColumn("lastName").setHeaderCaption("Фамилия");
        buttons.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addClickListener(e -> {
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
                UserRole userRole = new UserRole(admin.getValue(), view.getValue(),add.getValue(),confirm.getValue());
                userService.setRole(userRole);
                userNew.setUserRole(userRole);
                newPasswordIsTooSmall.setVisible(false);
                if(!password.getValue().isEmpty()) {
                    userNew.setFirstName(firstName.getValue());
                    userNew.setLastName(lastName.getValue());
                    userNew.setContact(contact.getValue());

                    if(userService.haveUser(email.getValue())){
                        Notification.show("Внимание!",
                                "Пользователь с таким e-mail уже существует!",
                                Notification.Type.TRAY_NOTIFICATION.WARNING_MESSAGE);
                        window.close();
                        return;
                    }
                        userNew.setEmail(email.getValue());
                   // String checkPasswordLength;
                   // checkPasswordLength=password.toString();
                    if (password.getValue().length() <= 4) {
                        Notification.show("Внимание!",
                                "пароль должен быть не менее 5 символов!",
                                Notification.Type.TRAY_NOTIFICATION.WARNING_MESSAGE);
                        //newPasswordIsTooSmall.setVisible(true);
                        password.clear();
                        window.close();
                        return;
                        }

                    userNew.setPasswordHash(DigestUtils.md5Hex(password.getValue()));
                }
                newPasswordIsTooSmall.setVisible(false);
                userService.addUser(userNew);
                changeUser.setVisible(false);
                userRoleChange.setVisible(false);
                listUsers(null);
                window.close();
            });
            no.addClickListener(u->{
                window.close();
            });

        });
       // addBtn.addClickListener(e -> editUser(new User("","")));
        delete.addClickListener(e -> {
            final Window window = new Window("");
            window.setWidth(450.0f, Unit.PIXELS);
            window.setPosition(400,150);
            Button ok = new Button("Да");
            Button no = new Button("Нет");
            HorizontalLayout buttons = new HorizontalLayout(ok,no);
            buttons.setSpacing(true);
            Label areSure = new Label("Вы уверены, что хотите удалить пользователя?");
            final FormLayout content = new FormLayout(areSure,buttons);

            window.setContent(content);
            UI.getCurrent().addWindow(window);
            ok.addClickListener(u->{
                userService.deleteUser(userNew);
                listUsers(null);
                window.close();
            });
            no.addClickListener(u->{
                window.close();
            });

        });
        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
               changeUser.setVisible(false);
                userRoleChange.setVisible(false);

            } else {
                //changeUser.setVisible(true);
                userRoleChange.setVisible(true);

                //changeUser.setVisible(true);
                this.editUser((User) grid.getSelectedRow());
            }
        });
        filter.setInputPrompt("Отфильтровать по фамилии");
        filter.addTextChangeListener( e-> listUsers(e.getText()));
        addNewBtn.addClickListener(e -> {
            userRoleChange.setVisible(true);
            changeUser.setVisible(true);
            editUser(new User("","","",userRole,"",""));
            //userRoleChange.setVisible(false);
            //changeUser.setVisible(false);
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
