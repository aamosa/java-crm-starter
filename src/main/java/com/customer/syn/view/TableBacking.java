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
    

    // ------------------------------------------------ constructors

    public TableBacking() {}

    /** Update the entity instance. */
    public String update(Contact ce) {
        contactService.mergeEntity(ce);
        ce.setEditable(false);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("ID#: " + ce.getId() + " updated."));
        return null;
    }

    /** Delete the entity instance */
    public String delete(Contact ce) {
        contactService.deleteEntity(ce.getId());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("ID#: " + ce.getId() + " deleted."));
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash()
        .setKeepMessages(true);
        return "index?faces-redirect=true";
    }

}
