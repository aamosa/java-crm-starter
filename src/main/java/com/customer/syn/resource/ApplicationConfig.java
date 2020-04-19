package com.customer.syn.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(loginPage = "/login.xhtml",
                                           useForwardToLogin = false,
                                           errorPage = ""))
@ApplicationScoped
public class ApplicationConfig {

}