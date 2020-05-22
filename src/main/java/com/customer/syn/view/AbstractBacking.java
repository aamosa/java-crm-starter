package com.customer.syn.view;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.model.BaseEntity;
import com.customer.syn.service.BaseRepositoryImpl;

public abstract class AbstractBacking<E extends BaseEntity<T>, T extends Number> {

    protected T Id;
    protected List<E> entities;
    
    @Inject
    private FacesContext facesContext;
    
    @Inject
    private ExternalContext ec;
    
    
    // ---------------------------------------------- constructors
    
    public AbstractBacking() {
    }
    
    
    @PostConstruct
    public void setup() {
        entities = getService().fetchAll();
    }

    protected abstract BaseRepositoryImpl<E, T> getService();
    
    
    /** Find by Id */
    protected Optional<E> findById(T id) {
        return getService().findByID(id);
    }
    
    /** Save */
    protected void save(E entity) {
        try {
            getService().save(entity);
        } catch (Exception e) {
            // :TODO
        }
    }
    
    
    /** :TODO move to utility class */
    public void addMsg(String msg) {
        ec.getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(msg);
        facesContext.addMessage(null, message);
    }
    
    
    // ---------------------------------------------- setters and getters
    
    public T getId() {
        return Id;
    }

    public void setId(T Id) {
        this.Id = Id;   
    }
    
    public List<E> getEntities() {
        return entities;
    }
    
}