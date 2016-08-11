package factory_bd.service;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ListSelect;
import factory_bd.entity.*;
import factory_bd.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Валерий on 08.08.2016.
 */
public class RequestService {
    private RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public void addRequest(Request request){
        requestRepository.save(request);
    }

    public Request getRequest(Request request){
        return request;
    }

    public void deleteRequest(Request request){
        requestRepository.delete(request);
    }

    public void setCompanyToRequest(Request request,Company company){
        request.setCompany(company);
    }

    public Request createNewRequest(Request request,Company company)
    {
        return new Request(company);
    }

    public void setPersonsList(Request request,List<Person> personsList ){
        request.setPersons(personsList);
    }

    public void setCarList(Request request,List<Car> carList){
        request.setCars(carList);
    }

    public void setDateFrom (Request request,Date dateFrom){
        request.setDateFrom(dateFrom);
    }

    public void setDateTo(Request request,Date dateTo){
        request.setDateTo(dateTo);
    }

    public void setDescription(Request request,String description){
        request.setDescription(description);
    }

    public void setApprovedBy(Request request, User user )
    {
        request.setApprovedBy(user);
        requestRepository.save(request);
    }
    public void setCreatedBy(Request request, User user )
    {
        request.setCreatedBy(user);

    }
    public void fillRequestGrid(Grid grid)
    {
        grid.setContainerDataSource( new BeanItemContainer(Request.class,requestRepository.findByAccepted(true)));
    }

}
