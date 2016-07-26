package factory_bd.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by Валерий on 25.07.2016.
 */
@SpringComponent
@UIScope
public class CompanyUI extends VerticalLayout{

    /*private CompanyRepository repository;
    private Company company;

    CompanyService editor = new CompanyService(repository);
    TextField companyName = new TextField("Company Name");
    TextField companyAdress = new TextField("Adress");
    TextField companyPhoneNumber = new TextField("Phone number");

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    CssLayout actions = new CssLayout(save, cancel, delete);

    @Autowired
    public CompanyUI(CompanyRepository repository){
        this.repository = repository;

        addComponents(companyName,companyAdress, companyPhoneNumber);

        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        save.addClickListener(e -> repository.save(company));
        delete.addClickListener(e-> repository.delete(company));
        cancel.addClickListener(e -> editor.editCompany(company));
        setVisible(false);
    }

    public interface ChangeHandler {

        void onChange();
    }
    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }*/

}
