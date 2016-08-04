package factory_bd.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.Renderer;
import com.vaadin.ui.themes.ValoTheme;
import factory_bd.entity.User;
import factory_bd.entity.UserRole;
import factory_bd.repository.UserRepository;
import factory_bd.repository.UserRoleRepository;
import factory_bd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by sereo_000 on 27.07.2016.
 */
@SpringComponent
@UIScope
public class AdminWindowView extends VerticalLayout implements View {
    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private User user;
    private UserRole userRole;
    private UserService userService;
    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final TextField email = new TextField("Email");
    private final TextField password = new TextField("Password");
    private final TextField contact = new TextField("Contact");
    private final CheckBox view = new CheckBox("View");
    private final CheckBox add = new CheckBox("Add");
    private final CheckBox confirm = new CheckBox("Confirm");
    private final CheckBox admin = new CheckBox("Admin");
    private final CheckBox changePasswordCheck = new CheckBox("Need to change password?");
    private final Grid grid = new Grid();
    private final Button addNewBtn;
    private final TextField filter;
    // Кнопки
    Button save = new Button("Save", FontAwesome.SAVE);
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
        userService=new UserService(userRepository);
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        HorizontalLayout userRoleChange = new HorizontalLayout(view,add,confirm,admin);
        VerticalLayout changeUser = new VerticalLayout(buttons,firstName,lastName,contact,email,password,changePasswordCheck);
        //VerticalLayout mainLayout = new VerticalLayout(actions, grid, changeUser);
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
        grid.setColumns("id", "firstName", "lastName","view","add","confirm","admin");
        Grid.Column viewColomn = grid.getColumn("view");
        Grid.Column addColomn = grid.getColumn("add");
        Grid.Column confirmColomn = grid.getColumn("confirm");
        Grid.Column adminColomn = grid.getColumn("admin");
        viewColomn.setRenderer((Renderer<?>) view);
        addColomn.setRenderer((Renderer<?>) add);
        confirmColomn.setRenderer((Renderer<?>) confirm);
        adminColomn.setRenderer((Renderer<?>) admin);
        filter.setInputPrompt("Filter by last name");
        buttons.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addClickListener(e -> editUser(user));
        delete.addClickListener(e -> userRepository.delete(user));
        // addUser.addClickListener(e -> addNewUser());
        //cancel.addClickListener(e -> editCustomer(customer));
        setVisible(false);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        init();
    }
    public void init() {
        filter.addTextChangeListener(e -> listUsers(e.getText()));
        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
            }
            else {
                editUser((User) grid.getSelectedRow());
            }
        });
        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> {

            userService.addUser(firstName.getValue(),lastName.getValue(),contact.getValue(),new UserRole(admin.getValue(),view.getValue(),add.getValue(),confirm.getValue()),email.getValue(),password.getValue());
        });
        // Listen changes made by the editor, refresh data from backend

        // Initialize listing
        listUsers(null);
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
