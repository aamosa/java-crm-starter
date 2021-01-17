package com.customer.syn.model;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Country {

    private String country;
    private String countryCode;

    // ----------------------------------------------------- constructors
    public Country() { /* no-args constructor */ }

    // ----------------------------------------------------- setters and getters
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Country))
            return false;
        Country country = (Country) o;
        return Objects.equals(this.country, country.country) &&
                Objects.equals(countryCode, country.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, countryCode);
    }
}
