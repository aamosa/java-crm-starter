package com.customer.syn.view.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.model.Role;
import com.customer.syn.model.User;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.RoleService;
import com.customer.syn.service.UserService;
import com.customer.syn.view.AbstractBacking;

@Named
@ViewScoped
public class UserBacking extends AbstractBacking<User, Long> implements Serializable {
   
    private static final long serialVersionUID = 691L;
    
    private User user;
    private String userName;
    private List<Role> userRoles;
    private Set<Role> selectedRoles;
    @Inject private UserService userService;
    

    // ------------------------------------------------------ constructors
    public UserBacking() { /* no-arg constructor */ }
    
    
    @PostConstruct
    public void init() { /* TODO: */ }


    public void initialize() {
        user = new User();
        setPage("create");
    }
    
    
    @Override
    protected BaseRepositoryImpl<User, Long> getService() {
        return userService;
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
        super.delete(user);
    }
    
    
    public String save() {
        userService.save(user);
        user.addRoles(new HashSet<>(getUserRoles()));
        if (log.isDebugEnabled()) {
            log.debug("[selected roles = {}]", getUserRoles());
        }
        super.save(user);
        return "user?faces-redirect=true&includeViewParams=true";
    }
    
    
    // ------------------------------------------------------ setters and getters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
