package com.customer.syn.resource.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
public class User extends BaseEntity<Integer> {

    private static final long serialVersionUID = 92L;

    private String email;
    private String firstName;
    private String lastName;

    @Column(columnDefinition = "CHAR(1) default 'A'")
    private String status;

    @NotNull
    @Column(nullable = false,
            unique = true)
    private String userName;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Column(updatable = false)
    private Instant lastLogin;

    @ManyToMany(fetch = FetchType.EAGER,
                cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "users_roles",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    
    // ----------------------------------------------------- constructors
    
    public User() {}
    
    
    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    
    // ----------------------------------------------------- setters and getters

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
