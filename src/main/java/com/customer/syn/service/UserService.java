package com.customer.syn.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.customer.syn.model.Role;
import com.customer.syn.model.User;

@Stateless
public class UserService extends BaseService<User, Long> {

    @Inject private RoleService roleService;

    private static final String ROLES_QUERY = "select r from Role r";
    private static final String USER_QUERY = "select u from User u where UPPER(u.userName) = UPPER(:username)";
    private static final String USER_PASS_QUERY = "select u from User u where u.userName = :user AND u.password = :pass";


    // ------------------------------------------------------------ persist operations
    @Override
    public void save(User user) {
        log.debug("<< userservice save invoked >>");
        Set<Role> selectedRoles = new HashSet<>();
        Iterator<Role> it = user.getRoles().iterator();
        while(it.hasNext()) {
            Role role = it.next();
            if (role.getId() == null) {
                break;
            }
            role = roleService.findByID(role.getId()); // make managed
            selectedRoles.add(role);
        }
        if (selectedRoles.size() > 0) {
            user.addRoles(selectedRoles);
        }
        getEntityManager().persist(user);
        log.debug("new user is persisted.");
    }

    // ------------------------------------------------------------ update operations
    public void updateLogin(User user) {
        user.setLastLogin(Instant.now());
        super.update(user);
    }

    // ------------------------------------------------------------ fetch operations
    public User findUserByRole(Role role) { /* TODO: */ return null; }

    public User findByUsername(String username) {
        User user = null;
        try {
            user = getEntityManager()
                .createQuery(USER_QUERY, User.class)
                .setParameter("username", username).getSingleResult();
        }
        catch (Exception e) { log.error("{}", e); }
        return user;
    }

    public Set<Role> getRoles() {
        return getEntityManager()
            .createQuery(ROLES_QUERY, Role.class)
            .getResultStream()
            .collect(Collectors.toSet());
    }

    public Optional<User> findByUserAndPass(String username, String password) {
        User user = null;
        try {
            user = getEntityManager()
                .createQuery(USER_PASS_QUERY, User.class)
                .setParameter("user", username)
                .setParameter("pass", password).getSingleResult();
            user.getRoles().size(); // initialize proxy
        }
        catch (Exception e) { /* TODO: */ }
        return Optional.ofNullable(user);
    }

}
