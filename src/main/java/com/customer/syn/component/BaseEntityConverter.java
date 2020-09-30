package com.customer.syn.component;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customer.syn.model.BaseEntity;


@FacesConverter(forClass = BaseEntity.class, managed = true)
public class BaseEntityConverter implements Converter<BaseEntity<Number>> {

    private final Logger log = LoggerFactory.getLogger(BaseEntityConverter.class);

    @PersistenceContext protected EntityManager em;

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, BaseEntity<Number> entity) {
        if (entity == null) return "";
        if (entity.getId() != null) {
            log.debug("converting to string Id {}", entity.getId());
            return entity.getId().toString();
        } 
        else {
            throw new ConverterException("Invalid Id.");
        }
    }
    

    @SuppressWarnings("unchecked")
    @Override
    public BaseEntity<Number> getAsObject(FacesContext ctx, UIComponent component, String Id) {
        if (Id == null || Id.isEmpty()) return null;
        ValueExpression value = component.getValueExpression("value");
        Class<?> type = value.getType(ctx.getELContext());
        try {
            log.debug("converting to object Id {} of type {}", Long.valueOf(Id), type);
            return (BaseEntity<Number>) em.find(type, Long.valueOf(Id));
        } 
        catch (Exception e) {
            throw new ConverterException("Invalid Id", e);
        }
    }
    

}
