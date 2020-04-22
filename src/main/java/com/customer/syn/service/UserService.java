package com.customer.syn.service;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.customer.syn.resource.model.User;

@Stateless
public class UserService {

    @PersistenceContext(name = "syn")
    EntityManager entityManager;

    public Optional<User> findById(Integer id) {
        User user = entityManager.find(User.class, id);
        return user != null ? Optional.of(user) : Optional.empty();
    }

    // find by username and password
    public Optional<User> findByUserandPassword(String username, String password) {
        try {
            User user = entityManager
                    .createQuery("SELECT u FROM User u WHERE u.userName = :user AND u.password = :pass", User.class)
                    .setParameter("user", username).setParameter("pass", password)
                    .getSingleResult();
            if (user != null)
                return Optional.of(user);
        } catch (Exception e) {
            // nothing
        }
        return Optional.empty();
    }
    
    // save
    public void save(User user) {
        try {
            entityManager.persist(user);
        } catch (Exception e) {
            // :TODO
        }
    }

    public List<User> fetchAll() {
        try {
            return entityManager.createQuery("SELECT u FROM User u", User.class)
                    .getResultList();
        } catch (Exception e) {
            // :TODO
        }
        return null;
    }

    public void delete() {
        // :TODO
    }
}
