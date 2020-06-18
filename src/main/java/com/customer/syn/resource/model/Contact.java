package com.customer.syn.resource.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Table
@Entity
public class Contact extends BaseEntity<Long> {

    private static final long serialVersionUID = -14L;
    
    @NotNull
    @ViewMeta(order = 1)
    private String firstName;
    
    @NotNull
    @ViewMeta(order = 2)
    private String lastName;
    
    @ViewMeta(order = 4)
    private String streetAddress;
    
    @ViewMeta(order = 5)
    private String city;
    
    @ViewMeta(order = 8)
    private String phone;
    
    @ViewMeta(order = 6)
    private String state;
    
    @Digits(fraction = 0,
            integer = 5)
    @ViewMeta(order = 7)
    private String zipCode;
    
    @Email
    @Column(unique = true)
    @ViewMeta(order = 3)
    private String email;

    @Transient
    private boolean editable;

    @Basic
    @Column(columnDefinition = "CHAR(1) default 'A'")
    @ViewMeta(order = 9,
              formField = false)
    private String statusCode = "A";
    
    
    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();

    
    
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

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
