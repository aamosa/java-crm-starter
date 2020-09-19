package com.customer.syn.view.contact;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.model.Contact;
import com.customer.syn.model.Task;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.ContactService;
import com.customer.syn.view.AbstractBacking;

@Named
@ViewScoped
public class ContactBacking extends AbstractBacking<Contact, Long> implements Serializable {
    
    private static final long serialVersionUID = 12L;
    
    private Contact contact;
    private List<Task> assignedTasks;
    
    @Inject
    private ContactService contactService;
   
   
    // --------------------------------------------------------- constructors
    public ContactBacking() {}

    
    @PostConstruct
    public void setUp() {}
    
    
    @Override
    protected BaseRepositoryImpl<Contact, Long> getService() {
        return contactService;
    }
    
    
    public void initialize() {
        contact = new Contact();
        setPage("create");
        if (log.isDebugEnabled())
            log.debug("contact object {} instantiated.", contact);
    }
    
    
    @Override
    public void view() {
        if (isSelected()) {
            setAssignedTasks(contactService.findTasksforContact(
                    getCurrentSelected()));
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
