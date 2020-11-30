package com.customer.syn.component;

import com.customer.syn.model.BaseEntity;
import com.customer.syn.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

@Model
@SuppressWarnings({ "rawtypes" })
@FacesConverter(forClass=BaseEntity.class, managed=true)
public class BaseEntityConverter implements Converter {

    @Inject private FacesContext fc;
    @PersistenceContext private EntityManager em;

    private static final String ID_ERR = "Entity Id is invalid.";
    private static final Logger log = LoggerFactory.getLogger(BaseEntityConverter.class);


    public BaseEntityConverter() { /* no-args constructor */ }


    // ------------------------------------------------------------------- conversion methods
    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object entity) {
        if (entity == null)
            return "";
        if (entity instanceof BaseEntity) {
            if (log.isDebugEnabled()) {
                log.debug("[ Entity {} to String conversion ]", entity);
            }
            Number id = ( (BaseEntity<?>) entity).getId();
            return String.valueOf(id);
        }
        else {
            throw new ConverterException();
        }
    }


    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component,
            String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty())
            return null;

        Class<?> type;
        Object Id = Long.valueOf(submittedValue);
        Class<?> referencedType = (Class<?>) component.getAttributes().get("referencedType");
        if (referencedType != null) {
            type = referencedType;
        }
        else {
            type = component.getValueExpression("value").getType(ctx.getELContext());
        }
        log.debug("[ Referenced type: {} ]", type);

        try {
            BaseEntity entity = (BaseEntity) em.find(type, Id);
            if (log.isDebugEnabled()) {
                log.debug("[ String {} to {} conversion ]", submittedValue, entity);
            }
            return entity;
        }
        catch (Exception e) {
            String msg = e.toString();
            fc.addMessage(null, new FacesMessage(SEVERITY_ERROR, msg, msg));
            throw new ConverterException(e);
        }
    }


    // ------------------------------------------------------------------- helper methods

}


















