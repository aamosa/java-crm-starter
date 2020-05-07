package com.customer.syn.view;

import java.util.List;

import javax.annotation.PostConstruct;

import com.customer.syn.resource.model.BaseEntity;
import com.customer.syn.service.BaseRepositoryImpl;


public abstract class BaseRepoBean<E extends BaseEntity<T>, T extends Number> {

    protected T Id;
    protected String searchOption;
    protected List<E> entities;
    
    
    // ---------------------------------------------- constructors
    
    public BaseRepoBean() {}
    
    @PostConstruct
    public void setup () {
        entities = getService().fetchAll();
    }
            
    
    // ---------------------------------------------- abstract methods
    
    protected abstract BaseRepositoryImpl<E, T> getService();
    
    
    // ---------------------------------------------- setters and getters
    
    public T getId() {
        return Id;
    }

    public void setId(T Id) {
        this.Id = Id;   
    }

    public String getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(String searchOption) {
        this.searchOption = searchOption;
    }

    public List<E> getEntities() {
        return entities;
    }
    
}