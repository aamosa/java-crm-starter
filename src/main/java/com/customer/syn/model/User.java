package com.customer.syn.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.EnumType.STRING;
import static com.customer.syn.model.Enums.Status.NEW;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class User extends BaseEntity<Long> implements Serializable {

    private static final long serialVersionUID = 92L;

    @ViewMeta(order=4)
    @Column(unique=true)
    @Email private String email;
    
    @Transient private boolean editable;
    @ViewMeta(order=3) private String lastName;
    @ViewMeta(order=2) private String firstName;

    @ViewMeta(order=1)
    @Enumerated(STRING) private Enums.Status status = NEW;

    @ViewMeta(order=5)
    @Size(min=3, max=30)
    @Column(nullable=false, unique=true)
    private String userName;

    @ViewMeta(order=6)
    @Size(min=4, max=30)
    private String password;

    @ViewMeta(order=8, formField=false)
    private Instant lastLogin;

    @ManyToMany(fetch=LAZY, cascade={ PERSIST, MERGE })
    @JoinTable(name="USERS_ROLES",
               joinColumns=@JoinColumn(name="USER_ID", nullable=false),
               inverseJoinColumns=@JoinColumn(name="ROLE_ID"))
    private Set<Role> roles = new HashSet<>();
    
    
    // ------------------------------------------------------------- constructors
    public User() { /* no-args constructor */ }
    
    
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

    public Enums.Status getStatus() {
        return status;
    }

    public void setStatus(Enums.Status status) {
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
