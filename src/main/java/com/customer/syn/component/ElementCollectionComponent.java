package com.customer.syn.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;


@FacesComponent(value = "elementcollection")
public class ElementCollectionComponent extends UIInput
    implements NamingContainer {


    @Override
    public String getFamily() {
        return UINamingContainer.COMPONENT_FAMILY;
    }


}
