package com.customer.syn.resource.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@NamedQueries({ @NamedQuery(name = "Contact.getAll", query = "SELECT c FROM Contact c"),
        @NamedQuery(name = "Contact.getByLastName", query = "SELECT c FROM Contact c WHERE c.lastName LIKE :lastName"),
        @NamedQuery(name = "Contact.getByFullName", query = "SELECT c FROM Contact c WHERE c.firstName LIKE :firstName AND c.lastName LIKE :lastName"),
        @NamedQuery(name = "Contact.getByDateRange", query = "SELECT c FROM Contact c WHERE c.createdAt between :from AND :to") })
@Table(name = "contacts")
@Entity
public class Contact extends BaseEntity<Long> {

    private static final long serialVersionUID = -14L;
    
    @NotNull
    private String firstName;
    
    @NotNull
    private String lastName;
    
    private String streetAddress;
    private String city;
    private String phone;
    private String state;
    private String zipCode;
    private String email;

    @Transient
    private boolean editable;

    @Basic
    @Column(columnDefinition = "CHAR(1) default 'A'")
    private String statusCode;

    
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
}
