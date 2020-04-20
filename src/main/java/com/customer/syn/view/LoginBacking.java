package com.customer.syn.view;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@Named(value = "login")
@RequestScoped
public class LoginBacking implements Serializable {

    private static final long serialVersionUID = 3L;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private ExternalContext externalContext;

    @Inject
    private FacesContext facesContext;

    @NotNull
    private String user;

    @NotNull
    private String pass;

    public void login() throws IOException {
        AuthenticationStatus authStatus = securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(), (HttpServletResponse) externalContext.getResponse(),
                AuthenticationParameters.withParams().credential(new UsernamePasswordCredential(user, pass)));

        if (authStatus == SEND_CONTINUE) {
            facesContext.responseComplete();
        } else if (authStatus == SEND_FAILURE) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authentication Failed", null));
        } else if (authStatus == SUCCESS) {
            facesContext.addMessage(null, new FacesMessage("Login Sucessful"));
            externalContext.redirect("web/index.xhtml");
        } else if (authStatus == NOT_DONE) {
            // TODO:
        }
    }

    
    // ---------------------------------------------------- setters and getters

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
