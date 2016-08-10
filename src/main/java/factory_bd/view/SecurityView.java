package factory_bd.view;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import factory_bd.entity.Request;
import factory_bd.repository.RequestRepository;
import factory_bd.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Валерий on 10.08.2016.
 */
@SpringComponent
@UIScope
public class SecurityView extends VerticalLayout implements View {
    RequestRepository requestRepository;
    ListSelect requestList = new ListSelect("Select request to accept or refuse");

    Grid securityGrid;

    @Autowired
    public SecurityView(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
        this.securityGrid = new Grid();
    }

    public void init(){
        VerticalLayout mainLayout = new VerticalLayout(securityGrid);
        mainLayout.setVisible(true);
        securityGrid.setWidth(600f, Unit.PIXELS);
        securityGrid.setSizeFull();
        securityGrid.setColumns("id","company","persons","dateFrom","dateTo","approvedBy");

        RequestService requestService = new RequestService(requestRepository);
        requestService.fillRequestGrid(securityGrid);
        addComponent(mainLayout);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
