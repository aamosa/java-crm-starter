package com.customer.syn.view.contact;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.resource.model.Task;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.ContactService;
import com.customer.syn.view.AbstractBacking;

@Named
@ViewScoped
public class ContactBacking extends AbstractBacking<Contact, Long> implements Serializable {
    
    private static final Logger log = Logger.getLogger(ContactBacking.class.getName());

    private static final long serialVersionUID = 12L;
    
    private Contact contact;
    
    private List<Task> assignedTasks;
    
    @Inject
    private ContactService contactService;
   
   
    
    // --------------------------------------------------------- constructors
    
    public ContactBacking() {}

    
    @PostConstruct
    public void init() { }
    
    
    @Override
    protected BaseRepositoryImpl<Contact, Long> getService() {
        return contactService;
    }
    
    
    public void initialize() {
        log.info("Contact object instantiated.");
        contact = new Contact();
    }
    
    
    @Override
    public void edit(Contact contact) {
        assignedTasks = contactService.getTaskforContact(contact);
        setPage("detail");
        super.edit(contact);
    }
     
    
    public String save() {
        super.save(contact);
        addMsg("New contact created!");
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

}
