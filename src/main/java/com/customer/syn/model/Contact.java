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
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
public class Contact extends BaseEntity<Long> implements Serializable {

    private static final long serialVersionUID = -14L;

    @Transient private boolean editable;

    @ViewMeta(order = 2)
    @NotNull private String lastName;

    @ViewMeta(order = 1)
    @NotNull private String firstName;

    @ViewMeta(order = 9)
    @Enumerated(STRING) private Enums.Status status = NEW;

    @ElementCollection
    @CollectionTable(name = "ADDRESS")
    @JoinColumn(name = "CONTACT_ID")
    private Set<Address> addresses = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "PHONE")
    @MapKeyEnumerated(STRING)
    @Column(name = "PHONE_NUMBER")
    private Map<Enums.PhoneType, String> phones = new HashMap<>();

    @ViewMeta(order = 3)
    @Column(unique = true)
    @Email private String email;

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

}
