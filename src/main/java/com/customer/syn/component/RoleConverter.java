package com.customer.syn.component;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customer.syn.model.Role;

/**
 * Custom {@link Converter} implementation for working with
 * <code>Customer</code> entity objects.
 */
@SuppressWarnings("rawtypes")
@FacesConverter(value = "roleConverter",
                managed = true)
public class RoleConverter implements Converter {
    
    private final Logger log = LoggerFactory.getLogger(RoleConverter.class);
    
    @Inject
    private FacesContext fc;
    
    @PersistenceContext
    private EntityManager em;

    
    @Override
    public Object getAsObject(FacesContext facescontext, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            log.debug("converting string {} to object.", value);
            return em.find(Role.class, Long.valueOf(value));
        } 
        catch (NumberFormatException e) {
            fc.addMessage(null, 
                    new FacesMessage(SEVERITY_ERROR, "Invalid Id", "Invalid Id"));
            throw new ConverterException();
        }
    }

    
    @Override
    public String getAsString(FacesContext facescontext, UIComponent component, Object value) {
        if (value == null) return "";
        
        log.debug("{}", value);
        
        if (value instanceof Role) {
            log.debug("converting {} to string.", value);
            return String.valueOf( ((Role) value).getId() );
        } 
        else {
            fc.addMessage(null,
                    new FacesMessage(SEVERITY_ERROR, "Conversion error", "Conversion error"));
            throw new ConverterException("Conversion Error.");
        }
    }

}
