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
    @Autowired
    private SpringViewProvider viewProvider;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);
        navigator.navigateTo(LoginScreenView.VIEW_NAME);
    }
}
