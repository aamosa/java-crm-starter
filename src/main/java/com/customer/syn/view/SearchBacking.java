package com.customer.syn.view;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.service.ContactService;


@Named("search")
@ViewScoped
public class SearchBacking extends BaseSearchBean<Contact> implements Serializable {

    private static final long serialVersionUID = 12L;
    
    private static final Logger log = Logger.getLogger(SearchBacking.class.getName());
    
    private Long contactId;
    private LocalDate searchDateTo;
    private LocalDate searchDateFrom;
    
    private List<Contact> entities;
    private List<Contact> values = new ArrayList<>();

    @Inject
    private FacesContext facesContext;
    
    @Inject
    private ContactService contactService;

    
    // ---------------------------------------------- constructors

    public SearchBacking() {}
    
    
    /**
     * Don't do extensive business logic in getter methods, getters are called by
     * JSF multiple times during the life-cycle events.
     * 
     */
    @PostConstruct
    public void init() {
        // :TODO lazy loading and pagnation
        entities = contactService.fetchAll();
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
            Contact contact = contactService.findByID(contactId).isPresent() ? contactService.findByID(contactId).get()
                    : null;
            
            if (values.size() > 0 ) values.clear();
            if (contact != null) values.add(contact);
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
            addMsg("No records found.");
        }
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

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
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
