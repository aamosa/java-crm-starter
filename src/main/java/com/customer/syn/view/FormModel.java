package com.customer.syn.view;

public class FormModel {

	private Object value;
	private String type;
	private String label;
	private String collectionType;

	// -------------------------------------------------------- constructors
	public FormModel() { /* no-args constructor */ }

	public FormModel(String type, String label, Object value) {
		this.label = label;
		this.type = type;
		this.value = value;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getCollectionType() {
		return this.collectionType;
	}

	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}


	@Override
	public String toString() {
		return "FormModel{" +
			"value=" + value +
			", type='" + type + '\'' +
			", label='" + label + '\'' +
			", collectionType='" + collectionType + '\'' +
			'}';
	}
}
