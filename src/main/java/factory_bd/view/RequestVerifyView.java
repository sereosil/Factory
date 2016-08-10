package factory_bd.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import factory_bd.entity.Request;
import factory_bd.repository.RequestRepository;
import factory_bd.service.RequestService;
import factory_bd.service.RequestVerifyService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Валерий on 09.08.2016.
 */
@SpringComponent
@UIScope
public class RequestVerifyView extends VerticalLayout implements View {
    RequestRepository requestRepository;
    ListSelect requestList = new ListSelect("Select request to accept or refuse");

    Button acceptButton;
    Button refuseButton;

    Request request;

    @Autowired
    public RequestVerifyView(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;

        acceptButton = new Button("Accept", FontAwesome.CHECK);
        refuseButton = new Button("Refuse",FontAwesome.TRASH_O);
    }

    public void init(){
       /* RequestService requestService = new RequestService(requestRepository);
        requestService.fillRequestList(requestList);*/
        RequestVerifyService requestVerifyService = new RequestVerifyService(requestRepository);
        requestVerifyService.fillRequestList(requestList);

        HorizontalLayout buttonLayout = new HorizontalLayout(acceptButton, refuseButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);

        VerticalLayout finalLayout = new VerticalLayout(requestList, buttonLayout);
        finalLayout.setSpacing(true);
        finalLayout.setMargin(true);
        finalLayout.setVisible(true);


        requestList.setRows((int) requestRepository.count());
        addComponent(finalLayout);

        requestList.addValueChangeListener( e -> {
            request = (Request)e.getProperty().getValue();
        });

        acceptButton.addClickListener( e-> {
            //requestService.setRequestCondition(request,true);
            requestVerifyService.setRequestCondition(request,true);

        });
        refuseButton.addClickListener( e->{
            //requestService.setRequestCondition(request,false);
            requestVerifyService.setRequestCondition(request,false);
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
