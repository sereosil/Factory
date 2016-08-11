package factory_bd.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import factory_bd.entity.Request;
import factory_bd.entity.User;
import factory_bd.repository.RequestRepository;
import factory_bd.service.RequestService;
import factory_bd.service.RequestVerifyService;
import org.springframework.beans.factory.annotation.Autowired;

import static factory_bd.view.LoginScreenView.SESSION_USER_KEY;

/**
 * Created by Валерий on 09.08.2016.
 */
@SpringView (name = RequestVerifyView.VIEW_NAME)
@SpringComponent
@UIScope
public class RequestVerifyView extends VerticalLayout implements View {
    public static final String VIEW_NAME ="REQUEST_VERIFY_VIEW" ;
    private User user;
    RequestRepository requestRepository;

    ListSelect requestList = new ListSelect("Выбирите запрос на подтверженние или отказ");

    Button acceptButton;
    Button refuseButton;
    Request request;

    @Autowired
    public RequestVerifyView(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;

        acceptButton = new Button("Подтвердить", FontAwesome.CHECK);
        refuseButton = new Button("Отказать",FontAwesome.TRASH_O);
    }
    public void update(){
        requestList.removeAllItems();
        RequestVerifyService requestVerifyService = new RequestVerifyService(requestRepository);
        //requestVerifyService.fillRequestList(requestList);
        requestVerifyService.fillRequestByFalse(requestList);
    }

    public void isEmptyCheck(){
        if (requestList.isEmpty() == true){
            requestList.addItem("Запросов больше нет");
        }
    }

    public void init(){
        RequestVerifyService requestVerifyService = new RequestVerifyService(requestRepository);
        RequestService requestService= new RequestService(requestRepository);
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
            requestVerifyService.setRequestCondition(request,true);

            requestService.setApprovedBy(request,user);
            requestList.removeItem(request);
            //isEmptyCheck();
        });
        refuseButton.addClickListener( e->{
            requestVerifyService.setRequestCondition(request,false);
            requestVerifyService.removeRequest(request);
            requestList.removeItem(request);
            //isEmptyCheck();
        });

    }
    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //this.user = (User) getUI().getSession().getAttribute(SESSION_USER_KEY);
        init();
    }
}
