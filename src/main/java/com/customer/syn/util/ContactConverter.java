package com.customer.syn.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.customer.syn.resource.EntityOperations;
import com.customer.syn.resource.model.Contact;

/**
 * Custom {@link Converter} implementation for working with
 * <code>Customer</code> entity objects.
 */
@SuppressWarnings("rawtypes")
@FacesConverter(value = "contactIdConverter", managed = true)
public class ContactConverter implements Converter {

    @Inject
    private EntityOperations entityoperations;

    @Override
    public Object getAsObject(FacesContext facescontext, UIComponent component, String value) {
        if (value == null || value.isEmpty())
            return null;

        try {
            return entityoperations.findByID(Long.valueOf(value));
        } catch (NumberFormatException nfe) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid Id", "invalid Id"));
            throw new ConverterException();
        }
    }

    @Override
    public String getAsString(FacesContext facescontext, UIComponent component, Object value) {
        if (value == null)
            return "";

        if (value instanceof Contact) {
            return String.valueOf(((Contact) value).getId());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "conversion error", "conversion error"));
            throw new ConverterException();
        }
    }

}
