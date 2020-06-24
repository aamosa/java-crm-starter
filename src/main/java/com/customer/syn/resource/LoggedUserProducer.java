package com.customer.syn.resource;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;

import com.customer.syn.resource.model.User;
import com.customer.syn.service.UserService;



@SessionScoped
public class LoggedUserProducer implements Serializable {
    
    private static final long serialVersionUID = -3410942127L;
    
    @Inject
    private SecurityContext securityContext;
    
    @Inject 
    private UserService userService;
    
    
    @Produces
    @LoggedUser
    @SessionScoped
    User getLoggedUser() {
        return userService.findByUsername(securityContext.getCallerPrincipal().getName());
    }

}
