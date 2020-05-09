package com.customer.syn.view.user;

import javax.inject.Inject;

import com.customer.syn.resource.model.User;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.UserService;
import com.customer.syn.view.AbstractBacking;

public class UserBacking extends AbstractBacking<User, Integer> {
    
    @Inject
    private UserService userService;

    // ---------------------------------------------- constructors
    
    public UserBacking() { }
    
    
    @Override
    protected BaseRepositoryImpl<User, Integer> getService() {
        return userService;
    }
    
}
