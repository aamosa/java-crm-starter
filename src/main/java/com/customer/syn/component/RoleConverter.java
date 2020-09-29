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


/** Custom faces-convertor implementation */
@SuppressWarnings("rawtypes")
@FacesConverter(value = "roleConverter",
                managed = true)
public class RoleConverter implements Converter {

    @Inject private FacesContext fc;
    @PersistenceContext private EntityManager em;

    private static final String ID_ERR = "Invalid entity Id";
    private static final String CONV_ERR = "Conversion Error";
    private final Logger log = LoggerFactory.getLogger(RoleConverter.class);


    // --------------------------------------------- conversion methods
    @Override
    public Object getAsObject(FacesContext facescontext, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            log.debug("[string value = {}, to object conversion]", value);
            return em.find(Role.class, Long.valueOf(value));
        } 
        catch (NumberFormatException e) {
            fc.addMessage(null, 
                    new FacesMessage(SEVERITY_ERROR, ID_ERR, ID_ERR));
            throw new ConverterException();
        }
    }

    
    @Override
    public String getAsString(FacesContext facescontext, UIComponent component, Object value) {
        if (value == null) return "";
        if (value instanceof Role) {
            log.debug("[object value = {}, to string conversion]", value);
            return String.valueOf( ((Role) value).getId() );
        } 
        else {
            fc.addMessage(null,
                    new FacesMessage(SEVERITY_ERROR, CONV_ERR, CONV_ERR));
            throw new ConverterException(CONV_ERR);
        }
    }

}
