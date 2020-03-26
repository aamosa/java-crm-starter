package com.customer.syn.view;

import static javax.faces.annotation.FacesConfig.Version.JSF_2_3;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.annotation.FacesConfig;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.Customer;
import com.customer.syn.resource.EntityOperations;

@FacesConfig(
        // Activates CDI build-in beans
        version = JSF_2_3)
@Named("entityUserBean")
@ViewScoped
public class SearchBacking implements Serializable {

    private static final long serialVersionUID = 12L;

    private int customerID;
    private String firstName;
    private String lastName;
    private String searchOption;
    private LocalDate searchDateTo;
    private LocalDate searchDateFrom;

    private List<Customer> customers;
    private List<Customer> values;

    @Inject
    private EntityOperations entityoperations;
    
    public SearchBacking() {}
    
    /**
     * Don't do extensive business logic in getter methods, getters are called by
     * JSF multiple times during the life-cycle events.
     * 
     * @see <a href=
     *      "https://stackoverflow.com/questions/2090033/why-jsf-calls-getters-multiple-times/2090062#2090062">
     *      Discussion on StackOverflow.com</a>
     */
    @PostConstruct
    public void init() {
        customers = entityoperations.findAll();
    }

    public void getCustomerByLastName(String name) {
        values = entityoperations.findByLastName(name);
    }

    public void getCustomerByFullName(String fName, String lName) {
        values = entityoperations.findByFullName(fName, lName);
    }

    /** Search */
    public void search() {
        switch (searchOption) {
        case "searchByName":
            if (!firstName.trim().isEmpty() && !lastName.trim().isEmpty())
                getCustomerByFullName(firstName.toUpperCase(), lastName.toUpperCase());
            else
                getCustomerByLastName(lastName.toUpperCase());
            break;
        case "searchByCustomerID":
            Customer ce = entityoperations.findByID(customerID);
            values = ce != null ? Arrays.asList(ce) : null;
            break;
        case "listAllCustomers":
            values = customers;
            break;
        case "searchByDate":
            values = entityoperations.findByDateRange(searchDateFrom, searchDateTo);
        case "search":
            return;
        default:
            return;
        }
    }
    
    /** Refresh */
    public void refresh(AjaxBehaviorEvent e) {
        this.values = null;
    }

    /** Clear */
    private void clear() {
        // TODO:
    }
    

    // ---------------------------------------------- setters and getters

    public List<Customer> getValues() {
        return values;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(String search) {
        searchOption = search;
    }

    public LocalDate getSearchDateTo() {
        return searchDateTo;
    }

    public void setSearchDateTo(LocalDate searchDateTo) {
        this.searchDateTo = searchDateTo;
    }

    public LocalDate getSearchDateFrom() {
        return searchDateFrom;
    }

    public void setSearchDateFrom(LocalDate searchDateFrom) {
        this.searchDateFrom = searchDateFrom;
    }

}
