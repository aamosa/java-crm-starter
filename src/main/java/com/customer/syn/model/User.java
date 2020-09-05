package com.customer.syn.model;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.MERGE;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.customer.syn.model.Contact.Status;

@Entity
public class User extends BaseEntity<Long> implements Serializable {

    private static final long serialVersionUID = 92L;

    @Email
    @ViewMeta(order = 4)
    @Column(unique = true)
    private String email;
    
    @ViewMeta(order = 2)
    private String firstName;
    
    @ViewMeta(order = 3)
    private String lastName;
    
    @Transient
    private boolean editable;
    
    @ViewMeta(order = 1,
              formField = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @NotNull
    @Size(min = 3, max = 30)
    @Column(nullable = false,
            unique = true)
    @ViewMeta(order = 5)
    private String userName;

    @NotNull
    @Size(min = 4, max = 30)
    @ViewMeta(order = 6)
    private String password;

    @ViewMeta(order = 8,
              formField = false)
    @Column(updatable = false)
    private Instant lastLogin;
    

    @ViewMeta(order = 7,
              formField = false)
    @ManyToMany(fetch = EAGER,
                cascade = { PERSIST, MERGE })
    @JoinTable(name = "users_roles",
               joinColumns = 
                   @JoinColumn(name = "user_id",
                               nullable = false),
               inverseJoinColumns = 
                   @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    
    // ------------------------------------------------------------- constructors
    
    public User() {}
    
    
    public User(String firstName, String lastName, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }
    
    
    // ------------------------------------------------------------- utility methods
    
    public void addRole(Role role) {
        getRoles().add(role);
        role.getUsers().add(this);
    }
    
    
    public void addRoles(Set<Role> roles) {
        setRoles(roles);
        for (Role role : roles) {
            role.getUsers().add(this);
        }
    }
    
    
    public void removeRole(Role role) {
        getRoles().remove(role);
        // role.getUsers().remove(this);
    }
    
    
    // ------------------------------------------------------------- setters and getters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
