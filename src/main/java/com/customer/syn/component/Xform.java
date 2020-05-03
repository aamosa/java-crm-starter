package com.customer.syn.component;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

@FacesComponent("xform")
public class Xform extends UIInput implements NamingContainer {

    public Xform() {
        this.getAttributes();
    }
    
    @Override
    public String getFamily() {
        return UINamingContainer.COMPONENT_FAMILY;
    }

    @Override
    protected Object getConvertedValue(FacesContext context, Object newSubmittedValue) throws ConverterException {
        return super.getConvertedValue(context, newSubmittedValue);
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
    }

    @Override
    public Object getSubmittedValue() {
        this.getValue();
        return super.getSubmittedValue();
    }
    
    
    
}
