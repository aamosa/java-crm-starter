package com.customer.syn.model;

import static com.customer.syn.model.Enums.Status.NEW;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.EnumType.STRING;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
public class Contact extends BaseEntity<Long> implements Serializable {

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
    
    //@ViewMeta(order = 8)
    @ElementCollection
    @CollectionTable(name = "PHONE")
    @MapKeyEnumerated(STRING)
    @Column(name = "PHONE_NUMBER")
    private Map<Enums.PhoneType, String> phones = new HashMap<>();
    
    @ViewMeta(order = 6)
    private String state;
    
    @Digits(fraction = 0, integer = 5)
    @ViewMeta(order = 7)
    private String zipCode;
    
    @Email
    @Column(unique = true)
    @ViewMeta(order = 3)
    private String email;

    @Transient private boolean editable;
    
    @ViewMeta(order = 9, formField = false)
    @Enumerated(STRING)
    private Enums.Status statusCode = NEW;

    @OneToMany(mappedBy = "contact", fetch = LAZY)
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

    public Enums.Status getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Enums.Status statusCode) {
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
