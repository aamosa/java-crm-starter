package com.customer.syn.resource;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

@NamedQueries({ 
        @NamedQuery(name = "Customer.getAll", query = "SELECT c FROM Customer c"),
        @NamedQuery(name = "Customer.getByLastName", query = "SELECT c FROM Customer c WHERE c.lastName LIKE :lastName"),
        @NamedQuery(name = "Customer.getByFullName", query = "SELECT c FROM Customer c WHERE c.firstName LIKE :firstName AND c.lastName LIKE :lastName"),
        @NamedQuery(name = "Customer.getByDateRange", query = "SELECT c FROM Customer c WHERE c.createdTime between :from AND :to")
})
@Table(name = "test_customers")
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = -14L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerID;

    private String firstName;
    private String lastName;
    private String streetAddress;
    private String city;

    @Column(name = "status_code", columnDefinition = "CHAR(1) default 'A'")
    private String statusCode;

    @Basic
    private String phone;
    
    @Basic
    private String state;

    @Basic
    private String zipCode;

    @Column
    private String email;

    @Column(name = "date_updated", nullable = false)
    private LocalDateTime lastUpdatedTime;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime createdTime;

    @Transient
    private boolean editable;

    @PrePersist
    private void onPersist() {
        lastUpdatedTime = createdTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
    }

    @PreUpdate
    private void onUpdate() {
        lastUpdatedTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
    }
    

    // ----------------------------------------------------- setters and getters

    public int getCustomerID() {
        return customerID;
    }

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

    public LocalDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
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
}