package com.customer.syn.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import com.customer.syn.resource.model.Role;
import com.customer.syn.resource.model.User;

@Stateless
public class UserService extends BaseRepositoryImpl<User, Long> {
    

    public Optional<User> findByUserandPassword(String username, String password) {
        try {
            User user = em
                    .createQuery("SELECT u FROM User u WHERE u.userName = :user AND u.password = :pass", User.class)
                    .setParameter("user", username)
                    .setParameter("pass", password)
                    .getSingleResult();
            if (user != null)
                return Optional.of(user);
        } catch (Exception e) {
            // silently ignore 
        }
        return Optional.empty();
    }
    
    
    public User findByUsername(String username) {
        User user = null;
        try {
            user = em.createQuery("select u from User u where u.userName = :username", User.class)
                    .setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            //:TODO
        }
        return user;
    }
    
    
    public Set<Role> getRoles() {
        return getEntityManager().createQuery("select r from Role r", Role.class)
                .getResultStream()
                .collect(Collectors.toSet());
    }
    
}
