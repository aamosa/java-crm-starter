package com.customer.syn.view;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

@Named(value = "login")
@RequestScoped
public class LoginBacking implements Serializable {

    private static final long serialVersionUID = 3L;

    @NotNull
    private String user;

    @NotNull
    private String pass;
    
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


}
