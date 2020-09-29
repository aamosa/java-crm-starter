package com.customer.syn.view;

import static com.customer.syn.view.SearchModel.SelectModel.BASE_CLASS;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customer.syn.component.ValueLabelHolder;
import com.customer.syn.model.Role;
import com.customer.syn.model.User;
import com.customer.syn.model.Contact.Status;
import com.customer.syn.service.UserService;

@Named(value = "menuBacking")
@ApplicationScoped
public class MenuManager implements Serializable {

    private static final long serialVersionUID = 54L;
    private static final Logger log = LoggerFactory.getLogger(MenuManager.class);

    @Inject private FacesContext fc;
    @Inject private UserService userService;
    @Inject private SearchManager searchManager;

    private Status[] status;
    private List<User> users;
    private Set<Role> rolesMenu;
    private List<ValueLabelHolder<String>> navMenu;

    private List<SearchModel.Field> searchFields;
    private List<SearchModel.SelectModel> searchOptions;

    
    // ---------------------------------------------------------------- constructors
    public MenuManager() { /* no-args constructor */ }

    
    @PostConstruct
    public void init() {  // TODO: load from config file or db here
        loadNavMenu();
        loadSelects();
        setUpBaseSearch();
        if (log.isDebugEnabled()) {
            log.debug("[{} postconstruct initialize]", getClass().getSimpleName());
        }
    }


    private void loadNavMenu() {
        navMenu = new ArrayList<>();
        navMenu.add(new ValueLabelHolder<>("Contacts", getServletPath().concat("contact.xhtml")));
        navMenu.add(new ValueLabelHolder<>("Users", getServletPath().concat("user.xhtml")));
        navMenu.add(new ValueLabelHolder<>("Tasks", getServletPath().concat("task.xhtml")));
    }


    private void loadSelects() {
        loadUserMenu();
        loadRoleMenu();
    }


    private void loadUserMenu() {   // TODO: reload / refresh when new entity created
        if (this.users == null) {
            this.users = userService.fetchAll();
        }
    }


    private void loadRoleMenu() {
        if (this.rolesMenu == null) {
            this.rolesMenu = userService.getRoles();
        }
    }

    
    private void setUpBaseSearch() {
        searchOptions = searchManager.getSearchOptions(BASE_CLASS);
        searchFields = searchManager.getSearchFields(BASE_CLASS);
    }
    
    
    private String getServletPath() {
        String path = fc.getExternalContext().getRequestServletPath();
        return path != null ? path.substring(0, path.lastIndexOf('/')+1) : "";
    }
    
    
    // ---------------------------------------------------------------- getters
    public List<ValueLabelHolder<String>> getMenu() {
        return navMenu;
    }

    public List<SearchModel.SelectModel> getSearchOptions() {
        return searchOptions;
    }

    public List<SearchModel.Field> getSearchFields() {
        return searchFields;
    }

    public List<User> getUsers() {
        return users;
    }

    public Set<Role> getRolesMenu() {
        return rolesMenu;
    }

    public Status[] getStatus() {
        if (status == null) {
            status = Status.values();
        }
        return status;
    }
    

}