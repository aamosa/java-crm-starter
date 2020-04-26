package com.customer.syn.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.annotation.ManagedProperty;


public abstract class BaseSearchBean<T> {

    protected long id;
    
    protected String firstName;
    protected String lastName;
    protected String searchOption;
    // protected List<T> entities;
    
    

    @PostConstruct
    public void init() {
        // TODO
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
