package com.customer.syn.view.contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.ContactService;
import com.customer.syn.view.AbstractBacking;
import com.customer.syn.view.ColumnModel;

@Named
@ViewScoped
public class ContactBacking extends AbstractBacking<Contact, Long> implements Serializable {
    
    private static final Logger log = Logger.getLogger(ContactBacking.class.getName());

    private static final long serialVersionUID = 12L;
    
    private Contact contact;
    
    @Inject
    private ContactService contactService;
   
    
    // ---------------------------------------------- constructors
    
    public ContactBacking() {}

    
    @PostConstruct
    public void init() {
        log.info("type arg is : " + getChildClass().getSimpleName());
    }
    
    
    @Override
    protected BaseRepositoryImpl<Contact, Long> getService() {
        return contactService;
    }
    
    
    public void initialize() {
        log.info("new Contact created");
        contact = new Contact();
    }
    
     
    public void edit(Contact c) {
        c.setEditable(true);
    }
    
    
    @Override
    public void update(Contact c) {
        super.update(c);
        c.setEditable(false);
    }
     
    
    public String save() {
        super.save(contact);
        addMsg("New contact created!");
        return "index?faces-redirect=true&includeViewParams=true";
    }
    
    

    // ---------------------------------------------- setters and getters

    
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

}
