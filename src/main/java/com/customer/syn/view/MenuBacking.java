package com.customer.syn.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.customer.syn.util.ValueLabelHolder;

@Named
@ApplicationScoped
public class MenuBacking implements Serializable {

    private static final long serialVersionUID = 54L;

    private List<ValueLabelHolder<String>> menu;
    private List<ValueLabelHolder<String>> searchOptions;

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
    }

    private void initSearchOptions() {
        searchOptions = new ArrayList<>();
        searchOptions.add(new ValueLabelHolder<>("Search By Name", "searchByName"));
        searchOptions.add(new ValueLabelHolder<>("Search By Date Range", "searchByDate"));
        searchOptions.add(new ValueLabelHolder<>("Search By Id", "searchByID"));
        searchOptions.add(new ValueLabelHolder<>("Display All", "fetchAll"));
    }

    
    // -------------------------------------------------- setters and getters

    public List<ValueLabelHolder<String>> getMenu() {
        return menu;
    }

    public List<ValueLabelHolder<String>> getSearchOptions() {
        return searchOptions;
    }

}