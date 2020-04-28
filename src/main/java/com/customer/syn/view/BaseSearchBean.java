package com.customer.syn.view;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.customer.syn.resource.model.Contact;


public abstract class BaseSearchBean<T> {

    protected long id;
    protected String firstName;
    protected String lastName;
    protected String searchOption;
    
    @Inject
    private FacesContext facesContext;
    

    @PostConstruct
    public void init() {
        // :TODO
    }
    
    
//    /** Update the entity instance. */
//    public String update(Contact ce) {
//        contactService.mergeEntity(ce);
//        ce.setEditable(false);
//        facesContext.addMessage(null, new FacesMessage("Id #: " + ce.getId() + " Updated."));
//        return null;
//    }
//
//    /** Delete the entity instance */
//    public String delete(Contact c) {
//        contactService.deleteEntity(c.getId());
//        facesContext.getExternalContext().getFlash().setKeepMessages(true);
//        facesContext.addMessage(null, new FacesMessage("Id #: " + c.getId() + " Deleted."));
//        return null;
////        return "index?faces-redirect=true";
//    }
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

//    public List<T> getEntities() {
//        return entities;
//    }
//
//    public void setEntities(List<T> entities) {
//        this.entities = entities;
//    }

}
