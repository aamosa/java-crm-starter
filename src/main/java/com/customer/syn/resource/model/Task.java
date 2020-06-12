package com.customer.syn.resource.model;

import javax.persistence.Entity;

@Entity
public class Task extends BaseEntity<Long> {

    private static final long serialVersionUID = 149L;
    
    private Contact contact;
    
    private User createdUser;
    
    private User assignedUser;
    
    private String description;
    
    
    public Task() {}
    
    
    // ----------------------------------------------------- setters and getters

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    

}
