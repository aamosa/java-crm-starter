package com.customer.syn.resource;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import com.customer.syn.model.Role;
import com.customer.syn.model.User;
import com.customer.syn.service.UserService;

@ApplicationScoped
public class AppIdentityStore implements IdentityStore {

    @Inject private UserService userService;


    @Override
    public CredentialValidationResult validate(Credential credential) {
        UsernamePasswordCredential upc = (UsernamePasswordCredential) credential;
        String userName = upc.getCaller();
        Password pass = upc.getPassword();
        Optional<User> optionalUser = userService.findByUserAndPass(userName,
                String.valueOf(pass.getValue()));   // TODO: password

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userService.updateLogin(user);
            return
                new CredentialValidationResult(user.getUserName(), user.getRoles().stream()
                    .map(Role::getRoleName).collect(Collectors.toSet()));
        } else {
            return INVALID_RESULT;
        }
    }
    
    
}
