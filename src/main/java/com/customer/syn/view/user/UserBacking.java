package com.customer.syn.view.user;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.model.User;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.UserService;
import com.customer.syn.view.AbstractBacking;

@Named
@ViewScoped
public class UserBacking extends AbstractBacking<User, Integer> implements Serializable {
   
    private static final long serialVersionUID = 691L;

    private static final Logger log = Logger.getLogger(UserBacking.class.getName());
    
    private User user;

    @Inject
    private UserService userService;
    

    // ---------------------------------------------- constructors

    public UserBacking() { }

    
    @Override
    protected BaseRepositoryImpl<User, Integer> getService() {
        return userService;
    }
    
    public void initialize() {
        log.info("new User created");
        user = new User();
    }
    
    
    
    // ---------------------------------------------- setters and getters
    
    /** Create :TODO */
    public String create() {
        save(user);
        addMsg("New User created!");
        return "user?faces-redirect=true&includeViewParams=true";
    }
    
    

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
