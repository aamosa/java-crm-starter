package com.customer.syn.view.contact;

import com.customer.syn.model.Contact;
import com.customer.syn.model.Task;
import com.customer.syn.service.BaseService;
import com.customer.syn.service.ContactService;
import com.customer.syn.view.AbstractBacking;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ContactBacking extends AbstractBacking<Contact, Long> implements Serializable {

    private static final long serialVersionUID = 1221579874527L;
    
    private String fName;
    private Contact contact;
    @NotNull private String lName;
    private List<Task> assignedTasks;
    @Inject private ContactService contactService;


    // --------------------------------------------------------- constructors
    public ContactBacking() { /* no-args constructor */ }

    @Override
    protected BaseService<Contact, Long> getService() {
        return contactService;
    }

    public void initialize() {
        contact = new Contact();
        setPage("create");
    }

    @Override
    protected void doSearch(String value) {
        if ("searchContactName".equals(value)) {
            if (getfName() != null && !getfName().trim().isEmpty()
                    && !getlName().trim().isEmpty()) {
                values = getService().findByFullName(getfName(), getlName());
            }
            else {
                values = getService().findByLastName(getlName());
            }
        }
    }

    @Override
    public void view() {
        if (isSelected()) {
            setAssignedTasks(contactService.findTasksforContact(getCurrentSelected()));
            setCurrentEntity(getCurrentSelected());
            setPage("detail");
        }
        else {
            addMsg(NO_SELECTION);
        }
    }

    @Override
    public void edit() {
        if (isSelected()) {
            super.edit(getCurrentSelected());
            setPage("editcontact");
        }
        else {
            addMsg(NO_SELECTION);
        }
    }

    @Override
    public String update(Contact contact) {
        super.update(contact);
        return "index?faces-redirect=true";
    }

    public String save() {
        super.save(contact);
        return "index?faces-redirect=true&includeViewParams=true";
    }

    // --------------------------------------------------------- setters and getters
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(List<Task> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

}
