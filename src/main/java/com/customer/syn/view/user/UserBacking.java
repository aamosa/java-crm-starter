package com.customer.syn.view.user;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.resource.model.Role;
import com.customer.syn.resource.model.User;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.UserService;
import com.customer.syn.view.AbstractBacking;

@Named
@ViewScoped
public class UserBacking extends AbstractBacking<User, Long> implements Serializable {
   
    private static final long serialVersionUID = 691L;
    private static final Logger log = Logger.getLogger(UserBacking.class.getName());
    
    private User user;
    private List<Role> userRoles;
    private Set<Role> selectedRoles;
    
//    private List<String> availRoles;

    @Inject
    private UserService userService;
    
    

    // ------------------------------------------------------ constructors

    public UserBacking() { }
    
    
    @PostConstruct
    public void init() { }

    
    @Override
    protected BaseRepositoryImpl<User, Long> getService() {
        return userService;
    }
    
    
    public void initialize() {
        user = new User();
    }
    
    
    @Override
    public void edit(User user) {
        super.edit(user);
        setPage("edituser");
    }
    
    
    public String update() {
        super.update(getCurrentEntity());
        return "user?faces-redirect=true";
    }
    
    
    @Override
    public void delete(User user) {
        for (Role role : user.getRoles()) {
            user.removeRole(role);
        }
        super.delete(user);
    }
    
    
    public String save() {
        user.addRoles(new HashSet<Role>(userRoles));
        super.save(user);
        log.log(Level.INFO, () -> "Roles selected: " + getUserRoles().toString());
        return "user?faces-redirect=true&includeViewParams=true";
    }
    
    
    
    // ------------------------------------------------------ setters and getters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<Role> userRoles) {
        this.userRoles = userRoles;
    }


    public Set<Role> getSelectedRoles() {
        return selectedRoles;
    }


    public void setSelectedRoles(Set<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

}
