package com.customer.syn.view;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.customer.syn.resource.model.BaseEntity;
import com.customer.syn.resource.model.Contact;
import com.customer.syn.resource.model.User;
import com.customer.syn.service.ContactService;

@Model
public class CreateBacking {

    private Contact contact;
    private String createWhat;
    
    @Inject
    private ContactService contactService;

    
    // ------------------------------------------------- constructors
    
    public CreateBacking() { }
    
    
    @PostConstruct
    public void init() {
        contact = new Contact();
    }
    
    
    /** Save */
    public String save() {
        contactService.save(this.contact);
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage("New entry created."));

        // keep messages in flash scope so they survive the redirect.
        FacesContext.getCurrentInstance().getExternalContext().getFlash()
            .setKeepMessages(true);
        
        // using POST-Redirect-GET pattern here to prevent re-execution on page reload!
        return "create?faces-redirect=true&includeViewParams=true";
    }

    
    // ------------------------------------------------- setters and getters

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getCreateWhat() {
        return createWhat;
    }

}
