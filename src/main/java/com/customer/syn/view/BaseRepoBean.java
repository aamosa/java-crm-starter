package com.customer.syn.view;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.customer.syn.resource.model.BaseEntity;
import com.customer.syn.resource.model.Contact;


public abstract class BaseRepoBean<E extends BaseEntity<Long>> {

    private long id;
    private String firstName;
    private String lastName;
    private String searchOption;
    
    @Inject
    private FacesContext facesContext;
    

    @PostConstruct
    public void init() {
        // :TODO
    }

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
