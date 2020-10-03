package com.customer.syn.model;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String street;
    private String zipCode;
    private String state;

    public Address() { /* no-args constructors */ }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
