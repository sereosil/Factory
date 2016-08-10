package factory_bd.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import factory_bd.entity.Company;
import factory_bd.entity.Person;
import factory_bd.repository.PersonRepository;
import factory_bd.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by Валерий on 27.07.2016.
 */
@SpringComponent
@UIScope
public class PersonView extends VerticalLayout implements View{

    private final PersonRepository personRepository;

    private Person person;
    public   Company selectedCompany;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField passportIdentification = new TextField("Pasport");
    TextField filterPerson;

    Label searchLabel;

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    Button addNewPersonButton;

    Grid personGrid;

    @Autowired
    public PersonView(PersonRepository personRepository) {
        this.personRepository = personRepository;
        this.addNewPersonButton = new Button("New person", FontAwesome.PLUS);
        this.filterPerson = new TextField();
        this.personGrid = new Grid();
        this.searchLabel = new Label("Search:");

    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {init();}

    public void init(){
        personGrid.setHeight(300,Unit.PIXELS);
        personGrid.setColumns("id","firstName","lastName","passportIdentification");


        filterPerson.setInputPrompt("Filter by last name");
        filterPerson.addTextChangeListener( e-> fillPersonGridByLastName(e.getText(),selectedCompany));


        HorizontalLayout personUpperHorizontalLayout = new HorizontalLayout(searchLabel,filterPerson,addNewPersonButton);
        personUpperHorizontalLayout.setSpacing(true);

        VerticalLayout personMiddleVerticalLayout = new VerticalLayout(personGrid);

        HorizontalLayout personActionButtonsLayout = new HorizontalLayout(save,delete,cancel);
        personActionButtonsLayout.setSpacing(true);

        VerticalLayout personLowerVerticalLayout = new VerticalLayout(firstName,lastName,passportIdentification,personActionButtonsLayout);
        personLowerVerticalLayout.setSpacing(true);
        personLowerVerticalLayout.setVisible(false);

        VerticalLayout personFinalVerticalLayout = new VerticalLayout(personUpperHorizontalLayout,
                personMiddleVerticalLayout,personLowerVerticalLayout);

        personFinalVerticalLayout.setVisible(true);
        personFinalVerticalLayout.setMargin(true);
        personFinalVerticalLayout.setSpacing(true);

        addComponent(personFinalVerticalLayout);


        personGrid.addSelectionListener(e ->{
            if(e.getSelected().isEmpty()){
                personLowerVerticalLayout.setVisible(false);
            }
            else{
                personLowerVerticalLayout.setVisible(true);
                this.editPerson((Person) personGrid.getSelectedRow());
            }
        });


        addNewPersonButton.addClickListener(e -> {
            editPerson(new Person("","",selectedCompany,""));
            personLowerVerticalLayout.setVisible(true);

        });

        save.setStyleName(ValoTheme.BUTTON_FRIENDLY);
       // save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        delete.setStyleName(ValoTheme.BUTTON_DANGER);

        PersonService personService = new PersonService(personRepository);

        save.addClickListener(e->{
            personService.addPeson(person);
            personLowerVerticalLayout.setVisible(false);
        });
        delete.addClickListener(e->{
            personService.deletePerson(person);
            personLowerVerticalLayout.setVisible(false);
        });
        cancel.addClickListener(e->{
            editPerson(person);
            personLowerVerticalLayout.setVisible(false);
        });

        //fillPersonGrid(null,null);
    }

    public interface ChangeHandler {

        void onChange();
    }

    //region maybe needed after
  /* public void fillPersonGrid(String text, Company selectedCompany){
        if(StringUtils.isEmpty(text)){
            personGrid.setContainerDataSource(new BeanItemContainer(Person.class, personRepository.findAll()));
        }
        else {
            personGrid.setContainerDataSource(new BeanItemContainer(Person.class,
                    //personRepository.findByFirstNameStartsWithIgnoreCaseAndCompany(text,selectedCompany)));
                    personRepository.findByCompany(selectedCompany)));
        }
    }*/
    //endregion

    public  void fillPersonGridByLastName(String text, Company selectedCompany){
        if(StringUtils.isEmpty(text)){
            personGrid.setContainerDataSource(new BeanItemContainer(Person.class,
                    personRepository.findByCompanyName(selectedCompany.getCompanyName())));
        }
        else {
            personGrid.setContainerDataSource(new BeanItemContainer(Person.class,
                    personRepository.findByLastNameStartsWithIgnoreCase(text)));
        }
    }
    public void fillPersonGridBySelectedCompany(Company selectedCompany){
        if (selectedCompany == null){
            personGrid.setContainerDataSource(new BeanItemContainer(Person.class,
                    personRepository.findByCompany(selectedCompany)));
        }
        else {
            personGrid.setContainerDataSource(new BeanItemContainer(Person.class,
                    personRepository.findByCompany(selectedCompany)));
        }
    }

    public final void editPerson(Person p){
        final boolean persisted = p.getId() != null;

        if (persisted){
            person = personRepository.findOne(p.getId());
        }
        else {
            person = p;
        }
        cancel.setVisible(persisted);

        BeanFieldGroup.bindFieldsUnbuffered(person,this);

        setVisible(true);

        save.focus();

        firstName.selectAll();

    }
    public void setChangeHandler(PersonView.ChangeHandler h) {
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }



}
