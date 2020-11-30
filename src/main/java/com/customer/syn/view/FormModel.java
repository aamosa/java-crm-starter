package com.customer.syn.view;

import com.customer.syn.model.FormInputType;

import java.util.Objects;

public class FormModel {

    private Object value;
    private String label;
    private String converter;
    private boolean isField;
    private FormInputType type;
    private String collectionType;
    private Object referencedValue;
    private Class<?> referencedType;


    // -------------------------------------------------------- constructors
    public FormModel() { /* no-args constructor */ }


    // -------------------------------------------------------- setters and getters
    public Class<?> getReferencedType() {
        return referencedType;
    }

    public void setReferencedType(Class<?> referencedType) {
        this.referencedType = referencedType;
    }

    public String getConverter() {
        return converter;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    public FormInputType getType() {
        return type;
    }

    public void setType(FormInputType type) {
        this.type = type;
    }

    public boolean isField() {
        return isField;
    }

    public void setIsField(boolean field) {
        isField = field;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getReferencedValue() {
        return referencedValue;
    }

    public void setReferencedValue(Object referencedValue) {
        this.referencedValue = referencedValue;
    }

    public String getCollectionType() {
        return this.collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FormModel model = (FormModel) o;
        return isField == model.isField &&
            Objects.equals(value, model.value) &&
            Objects.equals(label, model.label) &&
            Objects.equals(converter, model.converter) &&
            type == model.type &&
            Objects.equals(collectionType, model.collectionType) &&
            Objects.equals(referencedValue, model.referencedValue) &&
            Objects.equals(referencedType, model.referencedType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, label, converter, isField, type, collectionType,
            referencedValue, referencedType);
    }

    @Override
    public String toString() {
        return "FormModel[" +
            "value=" + value +
            ", label='" + label + '\'' +
            ", converter='" + converter + '\'' +
            ", isField=" + isField +
            ", type=" + type +
            ", collectionType='" + collectionType + '\'' +
            ", referencedValue=" + referencedValue +
            ", referencedType=" + referencedType +
            ']';
    }

}
