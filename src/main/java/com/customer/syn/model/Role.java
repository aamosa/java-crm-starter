package com.customer.syn.model;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Role extends BaseEntity<Long> implements Serializable {

    private static final long serialVersionUID = 6L;

    @Column(nullable = false)
    private String roleName;
    
    @ManyToMany(mappedBy = "roles", 
                fetch = EAGER)
    private Set<User> users = new HashSet<>();
    

    // ------------------------------------------------------- constructors

    public Role() {}
    
   
    public Role(String role) {
        this.roleName = role;
    }

    
    public Role(String roleName, User user) {
        this.roleName = roleName;
        this.getUsers().add(user);
    }
    
    
    // ------------------------------------------------------- setters and getters

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUsers() {
        return users;
    }

}
