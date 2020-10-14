package com.customer.syn.view;

import com.customer.syn.model.Enums.Status;
import com.customer.syn.model.Role;
import com.customer.syn.model.User;
import com.customer.syn.service.UserService;
import com.customer.syn.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
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
    @Inject private UserService userService;
    private static final Map<String, String> NAV_MENU = new HashMap<>();


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
        // TODO: rework
        NAV_MENU.put(Utils.getPath().concat("contact.xhtml"), "Contacts");
        NAV_MENU.put(Utils.getPath().concat("user.xhtml"), "Users");
        NAV_MENU.put(Utils.getPath().concat("task.xhtml"), "Tasks");
    }


    private void loadSelects() {
        // TODO: refresh when new user persisted
        if (this.users == null) {
            this.users = userService.fetchAll();
        }

        if (this.rolesMenu == null) {
            this.rolesMenu = userService.getRoles();
        }
    }


    public String pageTitle(String viewId) {
            return NAV_MENU.get(viewId);
        }


    public enum DataType {
        TEXT("text"),
        DATE("date"),
        NUMBER("number"),
        SELECT("select"),
        CHECKBOX("checkbox"),
        SECRET("secret"),
        MULTIPLE("multiple");

        String value;

        DataType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    
    // ---------------------------------------------------------------- read-only getters
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