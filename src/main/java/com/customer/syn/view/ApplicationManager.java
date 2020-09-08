package com.customer.syn.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.component.FormField;
import com.customer.syn.component.ValueLabelHolder;
import com.customer.syn.model.Role;
import com.customer.syn.model.User;
import com.customer.syn.model.Contact.Status;
import com.customer.syn.service.UserService;

@Named(value = "menuBacking")
@ApplicationScoped
public class ApplicationManager implements Serializable {

    private static final long serialVersionUID = 54L;

    @Inject
    private UserService userService;

    private Status[] status;
    private List<User> users;
    private Set<Role> availRoles;
    
    private static Map<String, Object> searchFieldValues;
    private static List<FormField> searchFields;
    private static List<ValueLabelHolder<String>> menu;
    private static List<ValueLabelHolder<String>> searchOptions;
    
    
    // ---------------------------------------------------------------- constructors
    
    public ApplicationManager() {}

    
    @PostConstruct
    public void init() {  // :TODO load from property file or DB here
        if (users == null) users = userService.fetchAll();
        if (availRoles == null) availRoles = userService.getRoles();
        
        menu = new ArrayList<>();
        menu.add(new ValueLabelHolder<>("Contacts", "index.xhtml"));
        menu.add(new ValueLabelHolder<>("Users", "user.xhtml"));
        menu.add(new ValueLabelHolder<>("Tasks", "task.xhtml"));
        initSearchOptions();
        initSearchFieldsAndValues();
    }

    
    private void initSearchOptions() {
        searchOptions = new ArrayList<>();
        searchOptions.add(new ValueLabelHolder<>("Name", "searchByName"));
        searchOptions.add(new ValueLabelHolder<>("Date", "searchByDate"));
        searchOptions.add(new ValueLabelHolder<>("ID", "searchByID"));
        searchOptions.add(new ValueLabelHolder<>("Display All", "fetchAll"));
    }
    
    
    private void initSearchFieldsAndValues() {
        searchFields = new ArrayList<FormField>();
        searchFields.add(new FormField("First Name", "firstName", "text", "searchByName", true));
        searchFields.add(new FormField("Last Name", "lastName", "text", "searchByName", true));
        searchFields.add(new FormField("From", "fromDate", "date", "searchByDate"));
        searchFields.add(new FormField("To", "toDate", "date", "searchByDate"));
        searchFields.add(new FormField("ID", "id", "number", "searchByID"));
        
        searchFieldValues = new HashMap<>();
        searchFieldValues.put("firstName", "firstName");
        searchFieldValues.put("lastName", "lastName");
        searchFieldValues.put("toDate", "searchDateTo");
        searchFieldValues.put("fromDate", "searchDateFrom");
        searchFieldValues.put("id", "id");
    }
    
    
    // ---------------------------------------------------------------- setters and getters

    public List<ValueLabelHolder<String>> getMenu() {
        return menu;
    }

    public List<ValueLabelHolder<String>> getSearchOptions() {
        return searchOptions;
    }

    public List<FormField> getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(List<FormField> searchFields) {
        ApplicationManager.searchFields = searchFields;
    }

    public Map<String, Object> getSearchFieldValues() {
        return searchFieldValues;
    }

    public void setSearchFieldValues(Map<String, Object> searchFieldValues) {
        ApplicationManager.searchFieldValues = searchFieldValues;
    }

    public List<User> getUsers() {
        return users;
    }

    public Set<Role> getAvailRoles() {
        return availRoles;
    }

    public Status[] getStatus() {
        if (status == null) {
            status = Status.values();
        }
        return status;
    }
    

}