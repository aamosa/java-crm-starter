package com.customer.syn.view;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.Customer;
import com.customer.syn.resource.EntityOperations;

@Named
@RequestScoped
public class TableBacking implements Serializable {

    private static final long serialVersionUID = 22L;

    @Inject
    private EntityOperations entityoperations;
    

    // ------------------------------------------------ constructors

    public TableBacking() {}

    /** Update the entity instance. */
    public String update(Customer ce) {
        entityoperations.mergeEntity(ce);
        ce.setEditable(false);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("ID#: " + ce.getCustomerID() + " updated."));
        return null;
    }

    /** Delete the entity instance */
    public String delete(Customer ce) {
        entityoperations.deleteEntity(ce.getCustomerID());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("ID#: " + ce.getCustomerID() + " deleted."));
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash()
        .setKeepMessages(true);
        return "index?faces-redirect=true";
    }

}
