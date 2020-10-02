package com.customer.syn.view;

import com.customer.syn.model.Contact.Status;
import com.customer.syn.model.Role;
import com.customer.syn.model.User;
import com.customer.syn.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@ApplicationScoped
@Named(value = "menuBacking")
public class MenuManager implements Serializable {

    private static final long serialVersionUID = 54L;
    private static final Logger log = LoggerFactory.getLogger(MenuManager.class);

    private List<User> users;
    private Set<Role> rolesMenu;
    private static final Map<String, String> NAV_MENU = new HashMap<>();

    @Inject private FacesContext fc;
    @Inject private UserService userService;
    @Inject private SearchManager searchManager;


    // ---------------------------------------------------------------- constructors
    public MenuManager() { /* no-args constructor */ }

    
    @PostConstruct
    public void init() {
        // TODO: load from config file or db here
        loadNavMenu();
        loadSelects();
        if (log.isDebugEnabled()) {
            log.debug("[{} postconstruct initialized]", getClass());
        }
    }


    private void loadNavMenu() {
        // TODO:
        NAV_MENU.put(getPath().concat("contact.xhtml"), "Contacts");
        NAV_MENU.put(getPath().concat("user.xhtml"), "Users");
        NAV_MENU.put(getPath().concat("task.xhtml"), "Tasks");
    }


    private void loadSelects() {
        // TODO: reload when new entity created
        if (this.users == null) {
            this.users = userService.fetchAll();
        }

        if (this.rolesMenu == null) {
            this.rolesMenu = userService.getRoles();
        }
    }


    private String getPath() {
        String path = fc.getExternalContext().getRequestServletPath();
        if (log.isDebugEnabled()) {
            log.debug("[ path = {} ]", path);
        }
        String viewId = fc.getViewRoot().getViewId();
        if (path != null) {
            path = path.substring(0, path.lastIndexOf('/') + 1);
            return path;
        }
        else {
            return "";
        }
    }


    public String pageTitle(String viewId) {
        return NAV_MENU.get(viewId);
    }
    
    
    // ---------------------------------------------------------------- getters
    public Map<String, String> getMenu() {
        return NAV_MENU;
    }

    public List<User> getUsers() {
        return users;
    }

    public Set<Role> getRolesMenu() {
        return rolesMenu;
    }

    public Status[] getStatus() {
        return Status.values();
    }

}