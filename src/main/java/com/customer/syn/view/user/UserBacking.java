package com.customer.syn.view.user;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
    private List<String> userRoles;
    private List<String> availRoles;

    @Inject
    private UserService userService;
    

    // ------------------------------------------------------ constructors

    public UserBacking() { }
    
    
    @PostConstruct
    public void init() { 
        availRoles = Arrays.asList("CAN_VIEW", "CAN_VIEW_EDIT", "CAN_DELETE");
    }

    
    @Override
    protected BaseRepositoryImpl<User, Long> getService() {
        return userService;
    }
    
    
    public void initialize() {
        log.info("User object instantiated.");
        user = new User();
    }
    
    
    @Override
    public void delete(User user) {
        for (Role role : user.getRoles()) {
            user.removeRole(role);
        }
        super.delete(user);
    }
    
    
    public String save() {
        Set<Role> selectedRoles = new HashSet<>();
        for (String s : userRoles) {
            Role role = new Role(s);
            selectedRoles.add(role);
        }
        
        user.addRoles(selectedRoles);
        super.save(user);
        addMsg("New User created!");
        log.info("roles selected: " + getUserRoles().toString());
        return "user?faces-redirect=true&includeViewParams=true";
    }
    
    
    
    // ------------------------------------------------------ setters and getters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }

    public List<String> getAvailRoles() {
        return availRoles;
    }

    public void setAvailRoles(List<String> availRoles) {
        this.availRoles = availRoles;
    }

}
