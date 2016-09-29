package ru.od.factory_bd.service;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;
import ru.od.factory_bd.entity.*;
import ru.od.factory_bd.repository.RequestRepository;

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
    public boolean doRepositoryHaveRequest(Request request){
        boolean check = false;
        if(request != null){
            for (Request req:requestRepository.findAll()){
                if( req.getCompany().equals(request.getCompany())  &&
                        req.getDateTo().getTime() == request.getDateTo().getTime()
                        /* && req.getPersons().equals(request.getPersons()) && *//*req.getCars().equals(request.getCars())*/ /*&& req.isAccepted()*/){
                    check = true;
                    break;
                }
                else {
                    check = false;
                }
            }
        }
        return check;
    }

}
