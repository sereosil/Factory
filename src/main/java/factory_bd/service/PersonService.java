package factory_bd.service;

import factory_bd.entity.Person;
import factory_bd.repository.PersonRepository;
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
}
