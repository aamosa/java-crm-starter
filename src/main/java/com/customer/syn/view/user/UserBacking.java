package com.customer.syn.view.user;

import com.customer.syn.model.Role;
import com.customer.syn.model.User;
import com.customer.syn.service.UserService;
import com.customer.syn.view.AbstractBacking;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named
@ViewScoped
public class UserBacking extends AbstractBacking<User, Long> implements Serializable {
   
    private static final long serialVersionUID = 31544781157113L;
    
    private User user;
    private String userName;
    private List<Role> userRoles;
    private Set<Role> selectedRoles;
    @Inject private UserService userService;

    // ------------------------------------------------------ constructors
    public UserBacking() { /* no-arg constructor */ }

    @Override
    protected UserService getService() {
        return userService;
    }

    public void initialize() {
        user = new User();
        setPage("create");
    }

    @Override
    protected void doSearch(String value) {
        if ("searchUserName".equals(value)) {
            if (!getUserName().isEmpty()) {
                User user = getService().findByUsername(getUserName());
                if (user != null) values = Arrays.asList(user);
            }
        }
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
        // userService.save(user);
        if (getUserRoles() != null
            && getUserRoles().size() > 0) {
            user.addRoles(new HashSet<>(getUserRoles()));
            if (log.isDebugEnabled())
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
