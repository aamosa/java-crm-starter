package com.customer.syn.resource;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;

import com.customer.syn.model.User;
import com.customer.syn.service.UserService;


@SessionScoped
public class LoggedUserProducer implements Serializable {
    
    private static final long serialVersionUID = -3410942127L;
    @Inject private UserService userService;
    @Inject private SecurityContext securityContext;
    
    
    @Produces
    @LoggedUser
    @SessionScoped
    User getLoggedUser() {
        User currentUser = userService.findByUsername(securityContext.getCallerPrincipal().getName());
        if (currentUser != null) {
            return currentUser;
        }
        else {
            throw new IllegalArgumentException("No logged in user exists.");
        }
    }
    

}
