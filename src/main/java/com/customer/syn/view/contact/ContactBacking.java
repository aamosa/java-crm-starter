package com.customer.syn.view.contact;

import java.io.Serializable;
import java.util.List;


import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        if (log.isDebugEnabled()) {
            log.debug("contact object instantiated.");
        }
    }
    
    
    @Override
    public void edit(Contact contact) {
        assignedTasks = contactService.findTasksforContact(contact);
        super.edit(contact);
        setPage("editcontact");
    }
    
    
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

}
