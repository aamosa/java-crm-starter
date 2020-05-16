package com.customer.syn.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.customer.syn.util.ValueLabelHolder;

@Named
@ApplicationScoped
public class MenuBacking implements Serializable {

    private static final long serialVersionUID = 54L;

    private static List<ValueLabelHolder<String>> menu;
    private static List<ValueLabelHolder<String>> searchOptions;
    private static List<ValueLabelHolder<String>> createOptions;

    
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
    }

    private void initSearchOptions() {
        searchOptions = new ArrayList<>();
        searchOptions.add(new ValueLabelHolder<>("Name", "searchByName"));
        searchOptions.add(new ValueLabelHolder<>("Date", "searchByDate"));
        searchOptions.add(new ValueLabelHolder<>("ID", "searchByID"));
        searchOptions.add(new ValueLabelHolder<>("Display All", "fetchAll"));
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

}