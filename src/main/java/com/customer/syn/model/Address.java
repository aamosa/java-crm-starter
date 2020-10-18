package com.customer.syn.model;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;

@Embeddable
public class Address {

    private String city;
    private String address1;
    private String address2;
    @Max(16) private String postalCode;
    private String stateProvince;
    @Embedded private Country country;

     @ManyToOne
     @JoinColumn(name="contact_id")
     private Contact contact;

    public Address() { /* no-args constructors */ }

    // ----------------------------------------------------- setters and getters
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }


}