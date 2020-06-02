package com.customer.syn.view;

import java.io.Serializable;

public class ColumnModel implements Serializable {

    private String header;
    private String property;

    
    // ---------------------------------------------------- constructors
    
    public ColumnModel(String header, String property) {
        this.header = header;
        this.property = property;
    }

    
    
    // ---------------------------------------------------- setters and getters

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
