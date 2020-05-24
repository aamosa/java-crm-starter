package com.customer.syn.service;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.customer.syn.resource.model.User;

@Stateless
public class UserService extends BaseRepositoryImpl<User, Integer> {

    @PersistenceContext(name = "syn")
    EntityManager em;


    /** Find by username and password */
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
    
    /** Find by username */
    public Optional<List<User>> findByUsername(String username) {
        return Optional.ofNullable(em.createQuery("select u from User u where upper(u.userName) = upper(:username)", User.class)
                        .setParameter("username", username)
                        .getResultList());
    }

}
