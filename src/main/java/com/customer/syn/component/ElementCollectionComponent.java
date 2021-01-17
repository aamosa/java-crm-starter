package com.customer.syn.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import java.util.HashSet;
import java.util.Set;


@FacesComponent(value = "elementcollection")
public class ElementCollectionComponent extends UIInput
    implements NamingContainer {

    private static final Logger LOG = LoggerFactory.getLogger(ElementCollectionComponent.class);

    private Object childObject;
    private Class<?> childClass;


    public Object getChildObject()
        throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> refType = (Class<?>) getAttributes().get("referencedType");
        Class<?> c = Class.forName(refType.getName());
        return
            c.newInstance();
    }


    public Class<?> getChildClass() {
        this.childClass = (Class<?>) getAttributes().get("referencedType");
        LOG.debug("inside ElmentCollection childClass = {}", childClass);
        return
            this.childClass;
    }


    @Override
    public Object getValue() {
        return
            super.getValue();
    }


    @Override
    public Object getSubmittedValue() {
        return
            super.getSubmittedValue();
    }


    @Override
    protected Object getConvertedValue(FacesContext facesContext, Object submittedValue)
        throws ConverterException {
        if (submittedValue == null) {
            return null;
        }

        try {
            Set set = new HashSet();
            set.add(submittedValue);
            return set;
        } catch (Exception e) {
            LOG.error("{}", e);
            throw new ConverterException();
        }
    }


    @Override
    public String getFamily() {
        return
            UINamingContainer.COMPONENT_FAMILY;
    }


}
