package factory_bd.service;

import com.vaadin.ui.ListSelect;
import factory_bd.repository.RequestRepository;
import factory_bd.entity.Request;
/**
 * Created by Валерий on 10.08.2016.
 */
public class RequestVerifyService {
    private RequestRepository requestRepository;

    public RequestVerifyService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public void fillRequestList(ListSelect requestList){
        for (Request request:requestRepository.findAll()){
            requestList.addItem(request);
        }
    }
    public void fillRequestByFalse(ListSelect requestList){
        for (Request request:requestRepository.findByAccepted(false)){
            requestList.addItem(request);
        }
    }
    public void setRequestCondition(Request request, boolean condition){
        request.setAccepted(condition);
        requestRepository.save(request);
    }
    public void removeRequest(Request request){
        requestRepository.delete(request);
    }
}
