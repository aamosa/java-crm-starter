package com.customer.syn.view;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.ContactService;


@Named("search")
@ViewScoped
public class SearchBacking extends BaseRepoBean<Contact, Long> implements Serializable {

    private static final long serialVersionUID = 12L;
    
    private String firstName;
    private String lastName;
    private LocalDate searchDateTo;
    private LocalDate searchDateFrom;
    private List<Contact> values = new ArrayList<>();

    @Inject
    private FacesContext facesContext;
    
    @Inject
    private ContactService contactService;

    
    // ---------------------------------------------- constructors
    
    public SearchBacking() { }

    
    @PostConstruct
    public void init() {
        getService();
    }

    
    @Override
    protected BaseRepositoryImpl<Contact, Long> getService() {
        return contactService;
    }
 
    
    /** Search */
    public void search() {
        switch (searchOption) {
        case "searchByName":
            if (!firstName.trim().isEmpty() && !lastName.trim().isEmpty())
                values = contactService.findByFullName(firstName.toUpperCase(), lastName.toUpperCase());
            else
                values = contactService.findByLastName(lastName.toUpperCase());
            break;
        case "searchByID":
            values = null;
            Contact contact = contactService.findByID(Id).isPresent() ? contactService.findByID(Id).get() : null;
            if (contact != null) values = new ArrayList<>(Arrays.asList(contact));
            break;
        case "fetchAll":
            values = entities;
            break;
        case "searchByDate":
            values = contactService.findByDateRange(searchDateFrom, searchDateTo);
            break;
        }
        
        if (values == null || values.size() < 1 )
            addMsg("No records found.");
    }
    
    /** Update */
    public void update(Contact c) {
        contactService.update(c);
        c.setEditable(false);
        addMsg("Contact Id #: " + c.getId() + " has been updated.");
    }
    
    /** Delete */
    public String delete(Contact c) {
        values.remove(c);
        contactService.deleteById(c.getId());
        addMsg("Contact Id #: " + c.getId() + " has been deleted!");
        if (values.size() == 0) {
            return "index?faces-redirect=true";
        }
        return null;
    }
    
    /** Edit */ 
    public void edit(Contact ce) {
        ce.setEditable(true);
    }
    
    
    // ---------------------------------------------- helper methods
    
    /** Refresh */
    public void refresh(AjaxBehaviorEvent e) {
        this.values = null;
    }
    
    public void addMsg(String msg) {
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(msg);
        facesContext.addMessage(null, message);
    }
    
    public static Contact findInList(final List<Contact> list, final Long Id) {
        return list.stream().filter(i -> i.getId().equals(Id)).findFirst().orElse(null);
    }
    

    // ---------------------------------------------- setters and getters

    public List<Contact> getValues() {
        return values;
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
