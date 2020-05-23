package com.customer.syn.view.contact;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.ContactService;
import com.customer.syn.view.AbstractBacking;

@Named
@ViewScoped
public class ContactBacking extends AbstractBacking<Contact, Long> implements Serializable {
    
    private static final Logger log = Logger.getLogger(ContactBacking.class.getName());

    private static final long serialVersionUID = 12L;
    
    private Contact contact;
    private List<Map<String, String>> columns = new ArrayList<>();
    
    @Inject
    private ContactService contactService;
   
    
    // ---------------------------------------------- constructors
    
    public ContactBacking() {}

    
    @PostConstruct
    public void init() {
        getService();
    }
    
    
    @Override
    protected BaseRepositoryImpl<Contact, Long> getService() {
        return contactService;
    }

    
    public void populateColumns() {
        Map<String, String> map = new HashMap<>();
    }
    
    
    public void initialize() {
        log.info("new Contact created");
        contact = new Contact();
        setCurrent("createcontact");
    }
    
    /** Edit */ 
    public void edit(Contact c) {
        c.setEditable(true);
    }
    
    
    @Override
    public void update(Contact c) {
        super.update(c);
        c.setEditable(false);
    }
    
    
    public void delete() {
    }
     
    
    public String save() {
        super.save(contact);
        addMsg("New contact created!");
        return "index?faces-redirect=true&includeViewParams=true";
    }
    
    
    // ---------------------------------------------- helper methods
    
    /** Refresh */
    public void refresh(AjaxBehaviorEvent e) {
       values = null;
    }
    
    public static Contact findInList(final List<Contact> list, final Long Id) {
        return list.stream().filter(i -> i.getId().equals(Id)).findFirst().orElse(null);
    }
    

    // ---------------------------------------------- setters and getters

    
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

}
