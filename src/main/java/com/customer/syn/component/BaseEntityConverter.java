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
public class BaseEntityConverter implements Converter {

    @PersistenceContext protected EntityManager em;
    private static final String ID_ERR = "Invalid entity Id";
    private static final Logger LOG = LoggerFactory.getLogger(BaseEntityConverter.class);


    @Override
    public String getAsString(FacesContext ctx,
                              UIComponent component,
                              Object entity) {
        if (entity == null)
            return "";
        if (entity instanceof BaseEntity) {
            Number id = ((BaseEntity) entity).getId();
            if (id != null) {
                LOG.debug("[Id {} to string conversion]", id);
                return id.toString();
            }
            return null;
        } else {
            throw new ConverterException(ID_ERR);
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object getAsObject(FacesContext ctx,
                              UIComponent component,
                              String str) {
        if ( str == null || str.isEmpty())
            return null;
        try {
            ValueExpression value = component.getValueExpression("value");
            Class<?> type = value.getType(ctx.getELContext());
            BaseEntity entity = (BaseEntity) em.find(type, Long.valueOf(str));
            if (LOG.isDebugEnabled()) {
                LOG.debug("[string '{}' to object {} conversion]",
                    Long.valueOf(str), type);
                LOG.debug("found entity: {}", entity);
            }
            return entity;
        }
        catch (Exception e) {
            LOG.error("{}",e);
            throw new ConverterException(e);
        }
    }
    

}
