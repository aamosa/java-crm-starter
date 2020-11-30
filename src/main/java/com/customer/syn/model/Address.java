package com.customer.syn.model;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.Objects;

@Embeddable
public class Address {

    @ViewMeta(order=3)
    private String city;

    @ViewMeta(order=1)
    private String address1;

    @ViewMeta(order=2)
    private String address2;

    @ViewMeta(order=6)
    private String country = "USA";

    @Size(max=40)
    @ViewMeta(order=4)
    private String stateProvince;

    @Size(max=16)
    @ViewMeta(order=5)
    private String postalCode;

    // @ManyToOne
    // @JoinColumn(name="contact_id")
    // private Contact contact;


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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @Override
    public String toString() {
        return "Address{city='" + city + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            ", country='" + country + '\'' +
            ", stateProvince='" + stateProvince + '\'' +
            ", postalCode='" + postalCode + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) &&
            Objects.equals(address1, address.address1) &&
            Objects.equals(address2, address.address2) &&
            Objects.equals(country, address.country) &&
            Objects.equals(stateProvince, address.stateProvince) &&
            Objects.equals(postalCode, address.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, address1, address2, country, stateProvince, postalCode);
    }
}