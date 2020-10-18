package com.customer.syn.view;

import com.customer.syn.model.FormInputType;

import java.util.Objects;

public class FormModel {

	private Object value;
	private String label;
	private boolean isField;
	private FormInputType type;
	private String collectionType;
	private Object referencedValue;

	// -------------------------------------------------------- constructors
	public FormModel() { /* no-args constructor */ }


	// -------------------------------------------------------- setters and getters
	public FormInputType getType() {
		return type;
	}

	public void setType(FormInputType type) {
		this.type = type;
	}

	public boolean isField() {
		return isField;
	}

	public void setField(boolean field) {
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
		FormModel formModel = (FormModel) o;
		return value.equals(formModel.value) &&
			type.equals(formModel.type) &&
			label.equals(formModel.label) &&
			Objects.equals(collectionType, formModel.collectionType) &&
			Objects.equals(referencedValue, formModel.referencedValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, type, label, collectionType, referencedValue);
	}

	@Override
	public String toString() {
		return "FormModel{" +
			"value=" + value +
			", type='" + type + '\'' +
			", label='" + label + '\'' +
			", collectionType='" + collectionType + '\'' +
			", referrencedValue=" + referencedValue +
			'}';
	}
}
