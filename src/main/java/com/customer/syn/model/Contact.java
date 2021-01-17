package com.customer.syn.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.customer.syn.model.Enums.Status.NEW;
import static com.customer.syn.model.FormInputType.ELEMENTCOLLECTION;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
public class Contact extends BaseEntity<Long>
    implements Serializable {

    private static final transient long serialVersionUID = -2687579851125L;

    @ViewMeta(order=3)
    @NotNull private String lastName;

    @ViewMeta(order=2)
    @NotNull private String firstName;

    @ViewMeta(order=4)
    @Column(unique=true)
    @Email private String email;

    @ViewMeta(order=1, formField=false)
    @Enumerated(STRING) private Enums.Status status = NEW;

    @OneToMany(mappedBy="contact", fetch=LAZY)
    private Set<Task> tasks = new HashSet<>(0);

    @ElementCollection
    @JoinColumn(name="contact_id")
    @CollectionTable(name="address")
    @ViewMeta(type=ELEMENTCOLLECTION)
    private Set<Address> addresses = new HashSet<>(0);

    @ElementCollection
    @CollectionTable(name="phone")
    @Column(name="phone_number")
    @MapKeyEnumerated(STRING)
    private Map<Enums.PhoneType, String> phones = new HashMap<>(0);

    @Transient
    private Address address = new Address();

    // ----------------------------------------------------- constructors
    public Contact() { /* no-args constructor */ }


    public Contact(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // ----------------------------------------------------- setters and getters
    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Map<Enums.PhoneType, String> getPhones() {
        return phones;
    }

    public void setPhones(Map<Enums.PhoneType, String> phones) {
        this.phones = phones;
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

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }


}
