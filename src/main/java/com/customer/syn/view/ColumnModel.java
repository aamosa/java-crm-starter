package com.customer.syn.view;

import java.io.Serializable;

public class ColumnModel implements Serializable {
    
    private static final long serialVersionUID = 5L;
    
    private String type;
    private String header;
    private String property;

    
    // ---------------------------------------------------- constructors
    public ColumnModel(String header, String property) {
        this.header = header;
        this.property = property;
    }
    
    public ColumnModel(String header, String property, String type) {
        this.header = header;
        this.property = property;
        this.type = type;
    }
    
    
    // ---------------------------------------------------- setters and getters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

}
