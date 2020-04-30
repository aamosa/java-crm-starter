package com.customer.syn.view;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.service.ContactService;

@Named
@RequestScoped
public class TableBacking implements Serializable {

    private static final long serialVersionUID = 22L;

    @Inject
    private ContactService contactService;
    
    @Inject
    private FacesContext facesContext;
    

    // ------------------------------------------------ constructors

    public TableBacking() {}
    

    /** Update */
    public String update(Contact c) {
        contactService.update(c);
        c.setEditable(false);
        facesContext.addMessage(null, new FacesMessage("Id #: " + c.getId() + " Updated."));
        return null;
    }

    
    /** Delete */
    public String delete(Contact c) {
//        contactService.deleteEntity(c.getId());
        contactService.deleteById(c.getId());
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        facesContext.addMessage(null, new FacesMessage("Id #: " + c.getId() + " Deleted!"));
        return null;
//        return "index?faces-redirect=true";
    }

}
