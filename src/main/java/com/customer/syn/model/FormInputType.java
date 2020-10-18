package com.customer.syn.model;

public enum FormInputType {
    TEXT("text"),
    DATE("date"),
    DATETIME("datetime"),
    NUMBER("number"),
    RADIO("radio"),
    SECRET("password"),
    TEXTBOX("textarea"),
    SELECT("select"),
    CHECKBOX("checkbox"),
    MULTISELECT("multiselect");

    String value;

    FormInputType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // quick and dirty
    public static FormInputType get(String value) {
        for (FormInputType d : values()) {
            if (d.value.equals(value))
                return d;
        }
        return null;
    }

}
