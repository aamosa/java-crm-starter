package com.customer.syn.view.login;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.security.enterprise.AuthenticationStatus.NOT_DONE;
import static javax.security.enterprise.AuthenticationStatus.SEND_CONTINUE;
import static javax.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static javax.security.enterprise.AuthenticationStatus.SUCCESS;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RequestScoped
@Named(value = "login")
public class LoginBacking implements Serializable {

    private static final long serialVersionUID = 3L;

    @NotNull private String user;
    @NotNull private String pass;
    @Inject private FacesContext fc;
    @Inject private ExternalContext ec;
    @Inject private SecurityContext securityContext;

    private static final String LOGOUT_URL = "login.xhtml";
    private static final String SUCCESS_URL = "/web/index.xhtml";


    public void login() throws IOException {
        AuthenticationStatus status = securityContext.authenticate( (HttpServletRequest) ec.getRequest(),
            (HttpServletResponse) ec.getResponse(),
            AuthenticationParameters.withParams()
                .newAuthentication(true)
                .credential(new UsernamePasswordCredential(user, pass)));
        if (status == SEND_CONTINUE) {
            fc.responseComplete();
        } 
        else if (status == SEND_FAILURE) {
            fc.addMessage(null, 
                    new FacesMessage(SEVERITY_ERROR, "Authentication Failed", null));
        } 
        else if (status == SUCCESS) {
            ec.redirect(ec.getRequestContextPath() + SUCCESS_URL);
        } 
        else if (status == NOT_DONE) { /* do nothing here */ }
    }

    
    /* logout */
    public void logout() throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        request.logout();
        request.getSession().invalidate();
        ec.redirect(LOGOUT_URL);
    }

    
    // ------------------------------------------------------- setters and getters
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
