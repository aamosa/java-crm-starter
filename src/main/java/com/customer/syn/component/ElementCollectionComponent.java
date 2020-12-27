package com.customer.syn.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;


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
        this.childClass = refType;

        return c.newInstance();
    }

    public Class<?> getChildClass() {
        this.childClass = (Class<?>) getAttributes().get("referencedType");
        LOG.debug("inside ElmentCollection childClass = {}", childClass);
        return
            this.childClass;
    }


    @Override
    public String getFamily() {
        return UINamingContainer.COMPONENT_FAMILY;
    }


}
