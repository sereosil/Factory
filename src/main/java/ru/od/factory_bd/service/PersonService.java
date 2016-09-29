package ru.od.factory_bd.service;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ListSelect;
import ru.od.factory_bd.entity.Company;
import ru.od.factory_bd.entity.Person;
import ru.od.factory_bd.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Валерий on 26.07.2016.
 */
public class PersonService {
    private PersonRepository personRepository;

    private Person person;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void addPeson(Person person){
        personRepository.save(person);
    }

    public void deletePerson(Person person){
        personRepository.delete(person);
    }

    public void fillPersonListInRequest(ListSelect listSelect, Company company){
        listSelect.removeAllItems();
        for (Person person : personRepository.findByCompany(company)) {
            listSelect.addItem(person);
        }

    }
    public void fillPersonGrid(Grid grid, Company company)
    {
        grid.setContainerDataSource( new BeanItemContainer(Person.class,personRepository.findByCompany(company)));
    }
    public boolean doRepositoryHavePerson(Person person){
        boolean check = false;
        for (Person personToFind:personRepository.findAll()){
            if(/* person.equals(personToFind) ||*/ person.getPassportIdentification().equals( personToFind.getPassportIdentification()) ){
                check = true;
                break;
            }
            else {
                check = false;

            }
        }
        return check;
    }
}
