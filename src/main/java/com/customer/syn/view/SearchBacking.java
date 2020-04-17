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

import com.customer.syn.resource.model.Contact;
import com.customer.syn.service.ContactService;

@FacesConfig(version = JSF_2_3) // Activates CDI build-in beans
@Named("search")
@ViewScoped
public class SearchBacking implements Serializable {

    private static final long serialVersionUID = 12L;

    private Long contactId;
    private String firstName;
    private String lastName;
    private String searchOption;
    private LocalDate searchDateTo;
    private LocalDate searchDateFrom;

    private List<Contact> values;
    private List<Contact> entities;

    @Inject
    private ContactService contactService;

    // ---------------------------------------------- constructors

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
        entities = contactService.findAll();
    }

    public void search() {
        switch (searchOption) {
        case "searchByName":
            if (!firstName.trim().isEmpty() && !lastName.trim().isEmpty())
                values = contactService.findByFullName(firstName.toUpperCase(), lastName.toUpperCase());
            else
                values = contactService.findByLastName(lastName.toUpperCase());
            break;
        case "searchByID":
            Contact ce = contactService.findByID(contactId);
            values = ce != null ? Arrays.asList(ce) : null;
            break;
        case "fetchAll":
            values = entities;
            break;
        case "searchByDate":
            values = contactService.findByDateRange(searchDateFrom, searchDateTo);
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
    public String edit(Contact ce) {
        ce.setEditable(true);
        return null;
    }

    /** Refresh */
    public void refresh(AjaxBehaviorEvent e) {
        this.values = null;
    }
   

    // ---------------------------------------------- setters and getters

    public List<Contact> getValues() {
        return values;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
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
