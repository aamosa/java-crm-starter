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
    private List<Role> userRoles;
    private Set<Role> selectedRoles;

    @Inject
    private UserService userService;
    

    // ------------------------------------------------------ constructors

    public UserBacking() { }
    
    
    @PostConstruct
    public void init() { }


    public void initialize() {
        user = new User();
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
//        User managedUser = userService.update(user);
//        Iterator<Role> it = managedUser.getRoles().iterator();
//        while (it.hasNext()) {
//            Role r = it.next();
//            managedUser.removeRole(r);
//        }
//        for (Role role : user.getRoles()) {
//            user.removeRole(role);
//        }
    }
    
    
    public String save() {
        userService.save(user);
        user.addRoles(new HashSet<>(getUserRoles()));
        if (log.isDebugEnabled())
            log.debug("user roles selected {}", getUserRoles());
        
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
