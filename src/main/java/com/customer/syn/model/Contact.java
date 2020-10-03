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

    @Transient private boolean editable;

    @ViewMeta(order = 2)
    @NotNull private String lastName;

    @ViewMeta(order = 1)
    @NotNull private String firstName;

    @ViewMeta(order = 4)
    private String streetAddress;
    
    @ViewMeta(order = 5)
    private String city;
    
    @ElementCollection
    @CollectionTable(name = "PHONE")
    @MapKeyEnumerated(STRING)
    @Column(name = "PHONE_NUMBER")
    private Map<Enums.PhoneType, String> phones = new HashMap<>();
    
    @ViewMeta(order = 6)
    private String state;
    
    @ViewMeta(order = 7)
    @Digits(fraction = 0, integer = 5)
    private String zipCode;

    @ViewMeta(order = 3)
    @Column(unique = true)
    @Email private String email;

    @ViewMeta(order = 9, formField = false)
    @Enumerated(STRING)
    private Enums.Status status = NEW;

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

    public Enums.Status getStatus() {
        return status;
    }

    public void setStatus(Enums.Status status) {
        this.status = status;
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
