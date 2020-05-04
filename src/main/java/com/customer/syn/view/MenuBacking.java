package com.customer.syn.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public MenuBacking() {
    }

    @PostConstruct
    public void init() {
        // :TODO load from property file or DB here
        menu = new ArrayList<>();
        menu.add(new ValueLabelHolder<>("Dashboard", "index.xhtml"));
        menu.add(new ValueLabelHolder<>("Create", "create.xhtml"));
        menu.add(new ValueLabelHolder<>("Settings", "setting.xhtml"));
        initSearchOptions();
        initCreateOptions();
    }

    private void initSearchOptions() {
        searchOptions = new ArrayList<>();
        searchOptions.add(new ValueLabelHolder<>("Search By Name", "searchByName"));
        searchOptions.add(new ValueLabelHolder<>("Search By Date Range", "searchByDate"));
        searchOptions.add(new ValueLabelHolder<>("Search By Id", "searchByID"));
        searchOptions.add(new ValueLabelHolder<>("Display All", "fetchAll"));
    }
    
    private void initCreateOptions() {
        createOptions = new ArrayList<>();
        createOptions.add(new ValueLabelHolder<>("Create new Contact", "createContact"));
        createOptions.add(new ValueLabelHolder<>("Create new Task", "createTask"));
        createOptions.add(new ValueLabelHolder<>("Create new Comment", "createComment"));
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