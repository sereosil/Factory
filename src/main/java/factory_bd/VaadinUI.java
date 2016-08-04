package factory_bd;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import factory_bd.entity.User;
import factory_bd.entity.UserRole;
import factory_bd.repository.UserRepository;
import factory_bd.repository.UserRoleRepository;
import factory_bd.view.AdminWindowView;
import factory_bd.view.LoginScreenView;
import factory_bd.view.UserSettingsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

import static factory_bd.view.LoginScreenView.SESSION_USER_KEY;

/**
 * Created by sereo_000 on 28.07.2016.
 */
@SpringUI
@Theme("valo")
public class VaadinUI extends UI {
    private final UserRepository userRepository;
    private final UserRoleRepository userRole;
    private User user;
    private LoginScreenView loginScreenView;
    private UserSettingsView userSettingsView;
    private AdminWindowView adminWindowView;
    @Autowired
    private SpringViewProvider viewProvider;
@Autowired
    public VaadinUI(UserRepository userRepository, UserRoleRepository userRole) {
        this.userRepository = userRepository;
        this.userRole=userRole;
        this.loginScreenView = new LoginScreenView(userRepository);
        this.userSettingsView = new UserSettingsView(userRepository,userRole);
        this.loginScreenView.init();

    //this.adminWindowView = new AdminWindowView(userRepository,userRole);
    //this.adminWindowView.init();
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //User user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        loginScreenView.setSizeFull();
        //userSettingsView.setSizeFull();
        setContent(loginScreenView);
        //setContent(userSettingsView);
        loginScreenView.init();
        userSettingsView.init();
       // setContent(userSettingsView);
        //adminWindowView.setSizeFull();
        //setContent(adminWindowView);
        //adminWindowView.init()
        Navigator navigator = new Navigator(this, loginScreenView);
        navigator.addProvider(viewProvider);
        navigator.addView(LoginScreenView.VIEW_NAME, LoginScreenView.class);
        navigator.addView(UserSettingsView.VIEW_NAME, UserSettingsView.class);
        //navigator.addView("TEST",UserSettingsView.class);
        navigator.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {

                // Check if a user has logged in
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof LoginScreenView;

                if (!isLoggedIn && !isLoginView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    getNavigator().navigateTo(LoginScreenView.VIEW_NAME);
                    return false;

                } else if (isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                    return false;
                }

                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {

            }
        });
    }
    @Override
    public void attach(){
        super.attach();

    }
}
