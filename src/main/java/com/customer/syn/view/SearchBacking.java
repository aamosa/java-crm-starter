package com.customer.syn.view;

import static javax.faces.annotation.FacesConfig.Version.JSF_2_3;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.annotation.FacesConfig;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.Customer;
import com.customer.syn.resource.EntityOperations;

@FacesConfig(version = JSF_2_3) // Activates CDI build-in beans
@Named("searchBacking")
@ViewScoped
public class SearchBacking implements Serializable {

    private static final long serialVersionUID = 12L;

    private int customerID;
    private String firstName;
    private String lastName;
    private String searchOption;
    private LocalDate searchDateTo;
    private LocalDate searchDateFrom;

    private List<Customer> values;
    private List<Customer> customers;

    @Inject
    private EntityOperations entityoperations;

    // ---------------------------------------------- constructors

    public SearchBacking() {
    }

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

    public void search() {
        switch (searchOption) {
        case "searchByName":
            if (!firstName.trim().isEmpty() && !lastName.trim().isEmpty())
                values = entityoperations.findByFullName(firstName.toUpperCase(), lastName.toUpperCase());
            else
                values = entityoperations.findByLastName(lastName.toUpperCase());
            break;
        case "searchByID":
            Customer ce = entityoperations.findByID(customerID);
            values = ce != null ? Arrays.asList(ce) : null;
            break;
        case "fetchAll":
            values = customers;
            break;
        case "searchByDate":
            values = entityoperations.findByDateRange(searchDateFrom, searchDateTo);
        case "search":
            return;
        default:
            return;
        }

        if (values == null || values.size() < 1) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage("No records found."));
        }
    }
    
    /** Edit */ 
    public String edit(Customer ce) {
        ce.setEditable(true);
        return null;
    }

    /** Refresh */
    public void refresh(AjaxBehaviorEvent e) {
        this.values = null;
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
