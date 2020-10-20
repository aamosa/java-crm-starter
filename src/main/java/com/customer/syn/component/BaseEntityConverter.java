package com.customer.syn.component;

import javax.el.ValueExpression;
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

import com.customer.syn.model.BaseEntity;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;


@FacesConverter(forClass = BaseEntity.class, managed = true)
public class BaseEntityConverter implements Converter {

    @Inject private FacesContext fc;
    @PersistenceContext protected EntityManager em;
    private static final String ID_ERR = "Entity Id is invalid.";
    private static final Logger log = LoggerFactory.getLogger(BaseEntityConverter.class);


    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object entity) {
        if (entity == null) return "";
        if (entity instanceof BaseEntity) {
            if (log.isDebugEnabled()) {
                log.debug("[object: {}, to string conversion]", entity);
            }
            Number id = ((BaseEntity) entity).getId();
            return String.valueOf(id);
        } else {
            throw new ConverterException();
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object getAsObject(FacesContext ctx, UIComponent component, String str) {
        if ( str == null || str.isEmpty()) return null;
        try {
            ValueExpression value = component.getValueExpression("value");
            Class<?> type = value.getType(ctx.getELContext());
            BaseEntity entity = (BaseEntity) em.find(type, Long.valueOf(str));
            if (log.isDebugEnabled()) {
                log.debug("[string {} to {} conversion]", str, entity);
            }
            return entity;
        }
        catch (Exception e) {
            String msg = e.getMessage();
            fc.addMessage(null, new FacesMessage(SEVERITY_ERROR, msg, msg));
            throw new ConverterException();
        }
    }
    

}
