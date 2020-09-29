package com.customer.syn.component;

public class ValueLabelHolder<T> {
    
    private String label;
    private T value;
    
    
    // ------------------------------------------------------ constructors
    public ValueLabelHolder(String label, T value) {
        this.label = label;
        this.value = value;
    }


    // ------------------------------------------------------ setters and getters
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

}
