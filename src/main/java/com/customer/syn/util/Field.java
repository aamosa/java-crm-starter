package com.customer.syn.util;

import java.util.Map;

public class Field {
    
    private String label;
    private String field;
    private String type;
    private String renderStr;
    
    public Field(String label, String field, String type, String renderStr) {
        this.label = label;
        this.field = field;
        this.type = type;
        this.renderStr = renderStr;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRenderStr() {
        return renderStr;
    }

    public void setRenderStr(String renderStr) {
        this.renderStr = renderStr;
    }

  
}
