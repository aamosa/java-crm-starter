package com.customer.syn.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import com.customer.syn.model.Role;
import com.customer.syn.model.User;

@Stateless
public class UserService extends BaseRepositoryImpl<User, Long> {
    
    private static final String ROLES_QUERY = "select r from Role r";
    private static final String USER_QUERY = "select u from User u where UPPER(u.userName) = UPPER(:username)";
    private static final String USER_PASS_QUERY = "select u from User u where u.userName = :user AND u.password = :pass";
    
    
    // ------------------------------------------------------------ business methods
    public Set<Role> getRoles() {
        return getEntityManager()
                .createQuery(ROLES_QUERY, Role.class)
                .getResultStream()
                .collect(Collectors.toSet());
    }
    
    
    public Optional<User> findByUsernameAndPass(String username, String password) {
        User user = null;
        try {
            user = getEntityManager().createQuery(USER_PASS_QUERY, User.class)
                    .setParameter("user", username)
                    .setParameter("pass", password)
                    .getSingleResult();
        } 
        catch (Exception e) { /* TODO: */ }
        return Optional.ofNullable(user);
    }
    
    
    public User findByUsername(String username) {
        User user = null;
        try {
            user = getEntityManager().createQuery(USER_QUERY, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } 
        catch (Exception e) { /* TODO: */ }
        return user;
    }
    
    
}
