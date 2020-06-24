package com.customer.syn.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.customer.syn.resource.model.BaseEntity;

@FacesConverter(forClass = BaseEntity.class,
                managed = true)
public class BaseEntityConverter implements Converter<BaseEntity<Number>> {

    private static final transient Logger log = Logger.getLogger(BaseEntityConverter.class.getName());

    @PersistenceContext
    protected EntityManager em;

    
    @Override
    public String getAsString(FacesContext ctx, UIComponent component, BaseEntity<Number> entity) {
        if (entity == null)
            return "";
        if (entity.getId() != null) {
            return entity.getId().toString();
        } else {
            throw new ConverterException("Invalid Id");
        }
    }
    

    @SuppressWarnings("unchecked")
    @Override
    public BaseEntity<Number> getAsObject(FacesContext ctx, UIComponent component, String Id) {
        if (Id == null || Id.isEmpty())
            return null;

        ValueExpression value = component.getValueExpression("value");
        Class<?> type = value.getType(ctx.getELContext());

        try {
            log.log(Level.INFO, () -> String.format("Id is %d - type is %s", Long.valueOf(Id), type));
            return (BaseEntity<Number>) em.find(type, Long.valueOf(Id));
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new ConverterException("Invalid Id", e);
        }
    }

}
