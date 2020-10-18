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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
@Named(value = "menuBacking")
public class MenuManager implements Serializable {

    private static final long serialVersionUID = 219321578524L;
    private static final Logger log = LoggerFactory.getLogger(MenuManager.class);

    private List<User> users;
    private Set<Role> rolesMenu;
    @Inject private UserService userService;
    @PersistenceContext private EntityManager em;

    private static final Map<String, String> NAV_MENU = new HashMap<>();


    // ---------------------------------------------------------------- constructors
    public MenuManager() { /* no-args constructor */ }

    
    @PostConstruct
    public void init() {
        loadNavMenu(); // TODO: load from config file or db here
        loadSelects();
        getMetaModel();
        if (log.isDebugEnabled()) {
            log.debug("[{} postconstruct initialized]", getClass());
        }
    }

    // debugging
    private void getMetaModel() {
        Metamodel model = em.getMetamodel();
        for (EntityType<?> type : model.getEntities()) {
            // log.debug("Javatype = {}", type.getJavaType().toString());
            for (Attribute<?, ?> attribute : type.getAttributes()) {
                if (attribute.isCollection()) {
                    PluralAttribute pa = (PluralAttribute) attribute;
                    // log.debug(" plural attribute - element type = {}", pa.getElementType());
                } else {
                    // log.debug("attribute = {}", attribute.toString());
                }
            }
        }
    }


    private void loadNavMenu() {
        // TODO: rework
        NAV_MENU.put(Utils.getPath().concat("contact.xhtml"), "Contacts");
        NAV_MENU.put(Utils.getPath().concat("user.xhtml"), "Users");
        NAV_MENU.put(Utils.getPath().concat("task.xhtml"), "Tasks");
    }


    private void loadSelects() {
        if (this.users == null) { // TODO: refresh it
            this.users = userService.fetchAll();
        }
        if (this.rolesMenu == null) {
            this.rolesMenu = userService.getRoles();
        }
    }

    public String pageTitle(String viewId) {
        return NAV_MENU.get(viewId);
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