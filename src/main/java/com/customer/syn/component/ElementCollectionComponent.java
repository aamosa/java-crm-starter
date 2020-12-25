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

    private Object childClass;


    public Object getChildClass()
    throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> refType = (Class<?>) getAttributes().get("referencedType");
        Class<?> c = Class.forName(refType.getName());
        LOG.debug("class = {}", c.getName());
        return c.newInstance();
    }

    @Override
    public String getFamily() {
        return UINamingContainer.COMPONENT_FAMILY;
    }


}
