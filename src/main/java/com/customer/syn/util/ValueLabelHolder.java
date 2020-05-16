package com.customer.syn.util;

public class ValueLabelHolder<T> {
    
    private String label;
    private T value;
    private boolean display;
    
    
    // ------------------------------------------------------ constructors
    
    public ValueLabelHolder(String label, T value) {
        this(label, value, false);
    }
    
    public ValueLabelHolder(String label, T value, boolean display) {
        this.label = label;
        this.value = value;
        this.display = display;
    }
    

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
    
    public boolean isDisplay() {
        return display;
    }
    
    public void setDisplay(boolean display) {
        this.display = display;
    }

}
