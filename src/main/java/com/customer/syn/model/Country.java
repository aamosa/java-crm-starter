package com.customer.syn.model;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Country {

    private String name;
    private String countryCode;


    public Country() { /* no-args constructor */ }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Country))
            return false;
        Country country = (Country) o;
        return Objects.equals(name, country.name) &&
                Objects.equals(countryCode, country.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, countryCode);
    }
}
