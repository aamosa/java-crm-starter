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
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
public class User extends BaseEntity<Integer> {

    private static final long serialVersionUID = 92L;

    @Email
    @ViewMeta(order = 4)
    private String email;
    
    @ViewMeta(order = 2)
    private String firstName;
    
    @ViewMeta(order = 3)
    private String lastName;
    
    @Transient
    private boolean editable;
    
    @ViewMeta(order = 1,
              formField = false)
    @Column(columnDefinition = "CHAR(1) default 'A'")
    private String status = "A";

    @NotNull
    @Column(nullable = false,
            unique = true)
    @ViewMeta(order = 5)
    private String userName;

    @NotNull
    @Column(nullable = false)
    @ViewMeta(order = 6)
    private String password;

    @ViewMeta(order = 8,
              formField = false)
    @Column(updatable = false)
    private Instant lastLogin;
    

    @ViewMeta(order = 7,
              formField = false)
    @ManyToMany(fetch = FetchType.EAGER,
                cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "users_roles",
               joinColumns = @JoinColumn(name = "user_id",
                                         nullable = false),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    
    
    // ----------------------------------------------------- constructors
    
    public User() {}
    
    
    
    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }
    
    
    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
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

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
