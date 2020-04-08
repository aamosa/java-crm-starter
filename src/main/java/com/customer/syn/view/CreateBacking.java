package com.customer.syn.view;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.Customer;
import com.customer.syn.resource.EntityOperations;

@Named
@RequestScoped
public class CreateBacking {

    @Inject
    private EntityOperations entityOperations;

    private String createWhat;

    private Customer customer = new Customer();

    /** Persist new entity instance */
    public String save() {
        entityOperations.save(this.customer);
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage("New entry created."));

        // keep messages in flash scope so they survive the redirect.
        FacesContext.getCurrentInstance().getExternalContext().getFlash()
            .setKeepMessages(true);
        
        // using POST-Redirect-GET pattern here to prevent re-execution on page reload!
        return "create?faces-redirect=true&includeViewParams=true";
    }

    // ---------------------------------------------- setters and getters

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCreateWhat() {
        return createWhat;
    }

    public void setCreateWhat(String createWhat) {
        this.createWhat = createWhat;
    }

}
