package factory_bd.view;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import factory_bd.entity.Company;
import factory_bd.repository.CarRepository;
import factory_bd.repository.CompanyRepository;
import factory_bd.repository.PersonRepository;
import factory_bd.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Валерий on 25.07.2016.
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
        //navigator.navigateTo(SpecialView.VIEW_NAME);

    }
}
