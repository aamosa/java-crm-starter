package com.customer.syn.resource;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import com.customer.syn.resource.model.Role;
import com.customer.syn.resource.model.User;
import com.customer.syn.service.UserService;

@FacesConfig @ApplicationScoped
public class AppIdentityStore implements IdentityStore {

    @Inject
    UserService userService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        UsernamePasswordCredential userPasswordCredential = (UsernamePasswordCredential) credential;
        String userName = userPasswordCredential.getCaller();
        Password password = userPasswordCredential.getPassword();

        Optional<User> optionalUser = userService.findByUserandPassword(userName, String.valueOf(password.getValue()));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new CredentialValidationResult(user.getUserName(),
                    user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));
        } else {
            return CredentialValidationResult.INVALID_RESULT;
        }
    }
}
