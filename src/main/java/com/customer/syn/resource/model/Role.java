package com.customer.syn.resource.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Role implements Serializable {

    private static final long serialVersionUID = 6L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer Id;

    @Column(nullable = false)
    private String roleName;

    @ManyToMany(mappedBy = "roles",
                cascade = CascadeType.PERSIST,
                fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();
    

    // ------------------------------------------- constructors

    public Role() {}
    
    public Role(String roleName) {
        this.roleName = roleName;
    }
    
    
    // ------------------------------------------- setters and getters
    
    public Integer getId() {
        return Id;
    }

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
