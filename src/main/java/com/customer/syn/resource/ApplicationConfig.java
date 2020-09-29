package com.customer.syn.resource;

import com.customer.syn.view.MenuManager;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.inject.Inject;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(loginPage = "/login.xhtml",
        errorPage = "",
        useForwardToLogin = false)
)
@FacesConfig
@ApplicationScoped
public class ApplicationConfig {

   @Inject
   private MenuManager menuManager;

}