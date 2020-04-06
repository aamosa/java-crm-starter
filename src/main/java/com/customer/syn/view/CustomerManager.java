package com.customer.syn.view;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.Customer;
import com.customer.syn.resource.EntityOperations;

@Named
@ViewScoped
public class CustomerManager implements Serializable {

    private static final long serialVersionUID = 22L;
    private static final Logger logger = Logger.getLogger(CustomerManager.class.getName());

    @Inject
    private EntityOperations entityoperations;

    private Customer customer;
    
    private boolean edit;
    

    @PostConstruct
    public void init() {
        // TODO:
    }

    /** Persist new entity instance */
    public String save() {
        entityoperations.save(customer);
        String msg = "New Customer Add!";
        FacesContext.getCurrentInstance().addMessage("newCustomerForm:addBtn", new FacesMessage(msg, msg));
        // keep messages in flash scope so they survive the redirect.
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "customer?faces-redirect=true&includeViewParams=true";
    }

    /** Update the entity instance. */
    public String update(Customer ce) {
        try {
            entityoperations.mergeEntity(ce);
            ce.setEditable(false);
            String msg = "ID #: " + ce.getCustomerID() + " updated.";
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg, msg));
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public void edit(Customer customer ) {
        this.customer = customer;
        edit = true;
    }

    /** Delete the entity instance */
    public String delete() {
        try {
            entityoperations.deleteEntity((Integer) customer.getCustomerID());
            String msg = "Customer with ID #: " + customer.getCustomerID() + " Deleted!";
            FacesContext.getCurrentInstance().addMessage("customerManager:deleteBtn", new FacesMessage(msg, msg));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            // using POST-Redirect-GET pattern here to prevent re-execution on page reload!
            return "customer?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------------------------------------ setters and getters

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
