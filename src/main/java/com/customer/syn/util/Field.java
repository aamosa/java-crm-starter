package com.customer.syn.util;


public class Field {
    
    private String label;
    private String fieldName;
    private String type;
    private String rendered;
    private boolean defaultDisplay;
    
    public Field(String label, String fieldName, String type, String rendered, boolean defaultDisplay) {
        this.label = label;
        this.fieldName = fieldName;
        this.type = type;
        this.rendered = rendered;
        this.defaultDisplay = defaultDisplay;
    }
    
    public Field(String label, String fieldName, String type, String rendered) {
        this(label, fieldName, type, rendered, false);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

    public boolean isDefaultDisplay() {
        return defaultDisplay;
    }

    public void setDefaultDisplay(boolean defaultDisplay) {
        this.defaultDisplay = defaultDisplay;
    }

}
