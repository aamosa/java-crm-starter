package com.customer.syn.view;

import java.util.ArrayList;
import java.util.List;

class SearchModel {

    public enum DataType {
        TEXT("text"),
        DATE("date"),
        NUMBER("number"),
        SELECT("select"),
        CHECKBOX("checkbox"),
        SECRET("secret");

        String value;

        DataType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // ------------------------------------------------------ constructors
    public SearchModel() { /* no-arg constructor */ }


    // -------------------------------------------- nested classes
    public static class SelectModel {
        private String label;
        private String value;
        private String clazz;
        public static final String BASE_CLASS = "base";
        public static final String VALUE_PREFIX = "search";
        private final List<Field> renderFields = new ArrayList<>();


        public SelectModel(String label) {
            this.label = label;
            this.value = label.indexOf(' ') >= 0 ?
                    VALUE_PREFIX + label.replaceAll("\\s", "") : VALUE_PREFIX + label;
            this.clazz = BASE_CLASS;
        }

        public void addFieldToRender(Field field) {
            renderFields.add(field);
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setClazz(String name) {
            this.clazz = name;
        }

        public String getClazz() {
            return this.clazz;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<Field> getRenderFields() {
            return renderFields;
        }

    }


    public static class Field {
        private String name;
        private String value;
        private String label;
        private String renderFor;
        private boolean show;
        private DataType type;

        public Field() { /* no-arg constructor */ }

        public Field(String value, String label, DataType type, boolean show) {
            this.value = value;
            this.label = label;
            this.type = type;
            this.show = show;
            this.name = label.indexOf(' ') >= 0 ?
                    label.replaceAll("\\s", "") : label;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setRenderFor(String renderFor) {
            this.renderFor = renderFor;
        }

        public String getRenderFor() {
            return this.renderFor;
        }

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public DataType getType() {
            return type;
        }

        public void setType(DataType type) {
            this.type = type;
        }

    }

}
