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

import com.customer.syn.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** FacesConvertor implementation for Role entity */
@FacesConverter(value = "roleConverter", managed = true)
public class RoleConverter implements Converter<Role> {

    @Inject private FacesContext fc;
    @PersistenceContext private EntityManager em;

    private static final String ID_ERR = "Entity Id is invalid.";
    private static final String CONV_ERR = "Conversion error";
    private final Logger log = LoggerFactory.getLogger(RoleConverter.class);


    // --------------------------------------------- conversion methods
    @Override
    public String getAsString(FacesContext facescontext, UIComponent component, Role value) {
        if (value == null) return "";
        try {
            if (log.isDebugEnabled()) {
                log.debug("[object: {}, to string conversion]", value);
            }
            return String.valueOf(value.getId());
        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(SEVERITY_ERROR, CONV_ERR, CONV_ERR));
            throw new ConverterException();
        }
    }

    @Override
    public Role getAsObject(FacesContext facescontext, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("[string: {}, to object conversion]", value);
            }
            return em.find(Role.class, Long.valueOf(value));
        } 
        catch (NumberFormatException e) {
            fc.addMessage(null, new FacesMessage(SEVERITY_ERROR, ID_ERR, ID_ERR));
            throw new ConverterException();
        }
    }

}
