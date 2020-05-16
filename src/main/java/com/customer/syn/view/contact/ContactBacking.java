package com.customer.syn.view.contact;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.annotation.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.ContactService;
import com.customer.syn.view.AbstractBacking;
import com.customer.syn.view.MenuBacking;

@Named
@ViewScoped
public class ContactBacking extends AbstractBacking<Contact, Long> implements Serializable {
    
    private static final Logger log = Logger.getLogger(ContactBacking.class.getName());

    private static final long serialVersionUID = 12L;
    
    private Contact contact = new Contact();
    private String firstName;
    private String lastName;
    private String searchOption;
    private LocalDate searchDateTo;
    private LocalDate searchDateFrom;
    
    private List<Contact> values = new ArrayList<>();
    
    private String page;
    
    
    @Inject
    private ContactService contactService;
   
    
    // ---------------------------------------------- constructors
    
    public ContactBacking() { }

    
    @PostConstruct
    public void init() {
        getService();
    }
    
    @Override
    protected BaseRepositoryImpl<Contact, Long> getService() {
        return contactService;
    }
    
    
    public void setCurrent(String page) {
        setPage(page);
    }
    
    public void reset() {
//        UIComponent comp = FacesContext.getCurrentInstance().getViewRoot().findComponent("ciform");
//        comp.setRendered(false);
        setPage(null);
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
            Contact contact = findById(Id).isPresent() ? findById(Id).get() : null;
            if (contact != null) values = new ArrayList<>(Arrays.asList(contact));
            break;
        case "fetchAll":
            values = entities;
            break;
        case "searchByDate":
            values = contactService.findByDateRange(searchDateFrom, searchDateTo);
            break;
        }
        
        if (values == null || values.size() < 1 ) {
            addMsg("No records found.");
        }
    }
    
    /** Edit */ 
    public void edit(Contact c) {
        c.setEditable(true);
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
    
    
    /** Create :TODO */
    public String create() {
        save(contact);
        addMsg("New contact created!");
        return "index?faces-redirect=true&includeViewParams=true";
    }
    
    
    // ---------------------------------------------- helper methods
    
    /** Refresh */
    public void refresh(AjaxBehaviorEvent e) {
        this.values = null;
    }
    
    public static Contact findInList(final List<Contact> list, final Long Id) {
        return list.stream().filter(i -> i.getId().equals(Id)).findFirst().orElse(null);
    }
    

    // ---------------------------------------------- setters and getters

    
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }


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

    public String getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(String searchOption) {
        this.searchOption = searchOption;
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

}
