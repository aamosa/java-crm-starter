package com.customer.syn.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class MenuBacking implements Serializable {

    private static final long serialVersionUID = 54L;
    private List<Page> menu;

    public MenuBacking() {}

    @PostConstruct
    public void init() {
        // :TODO load from property file or DB here
        menu = new ArrayList<>();
        menu.add(new Page("Dashboard", "/index.xhtml"));
        menu.add(new Page("Create", "/create.xhtml"));
    }

    // -------------------------------------------------- setters and getters

    public List<Page> getMenu() {
        return menu;
    }

    // -------------------------------------------------- inner class

    public class Page {

        private String title;
        private String view;

        public Page(String title, String view) {
            this.title = title;
            this.view = view;
        }

        public String getTitle() {
            return title;
        }

        public String getView() {
            return view;
        }
    }

}
