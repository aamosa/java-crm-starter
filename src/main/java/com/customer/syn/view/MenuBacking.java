package com.customer.syn.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.customer.syn.util.Field;
import com.customer.syn.util.ValueLabelHolder;

@Named
@ApplicationScoped
public class MenuBacking implements Serializable {

    private static final long serialVersionUID = 54L;

    private static List<ValueLabelHolder<String>> menu;
    private static List<ValueLabelHolder<String>> searchOptions;
    private static List<ValueLabelHolder<String>> createOptions;
    private static List<Field> searchFields;
    private static Map<String, Object> searchFieldValues;

    
    // -------------------------------------------------- constructors
    
    public MenuBacking() {}

    
    @PostConstruct
    public void init() {
        // :TODO load from property file or DB here
        menu = new ArrayList<>();
        menu.add(new ValueLabelHolder<>("Contacts", "index.xhtml"));
        menu.add(new ValueLabelHolder<>("Users", "user.xhtml"));
        menu.add(new ValueLabelHolder<>("Tasks", "task.xhtml"));
        menu.add(new ValueLabelHolder<>("Settings", "setting.xhtml"));
        initSearchOptions();
        initSearchFields();
    }

    private void initSearchOptions() {
        searchOptions = new ArrayList<>();
        searchOptions.add(new ValueLabelHolder<>("Name", "searchByName"));
        searchOptions.add(new ValueLabelHolder<>("Date", "searchByDate"));
        searchOptions.add(new ValueLabelHolder<>("ID", "searchByID"));
        searchOptions.add(new ValueLabelHolder<>("Display All", "fetchAll"));
    }
    
    
    private void initSearchFields() {
        searchFields = new ArrayList<Field>();
        searchFields.add(new Field("First Name", "firstName", "text", "searchByName", true));
        searchFields.add(new Field("Last Name", "lastName", "text", "searchByName", true));
        searchFields.add(new Field("From", "fromDate", "date", "searchByDate"));
        searchFields.add(new Field("To", "toDate", "date", "searchByDate"));
        searchFields.add(new Field("ID", "id", "number", "searchByID"));
        
        searchFieldValues = new HashMap<>();
        searchFieldValues.put("firstName", "firstName");
        searchFieldValues.put("lastName", "lastName");
        searchFieldValues.put("toDate", "searchDateTo");
        searchFieldValues.put("fromDate", "searchDateFrom");
        searchFieldValues.put("id", "id");
    }

    
    
    // -------------------------------------------------- setters and getters

    public List<ValueLabelHolder<String>> getMenu() {
        return menu;
    }

    public List<ValueLabelHolder<String>> getSearchOptions() {
        return searchOptions;
    }

    public List<ValueLabelHolder<String>> getCreateOptions() {
        return createOptions;
    }

    public List<Field> getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(List<Field> searchFields) {
        MenuBacking.searchFields = searchFields;
    }

    public Map<String, Object> getSearchFieldValues() {
        return searchFieldValues;
    }

    public void setSearchFieldValues(Map<String, Object> searchFieldValues) {
        MenuBacking.searchFieldValues = searchFieldValues;
    }
    
}