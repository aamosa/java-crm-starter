package com.customer.syn.resource.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Task extends BaseEntity<Long> implements Serializable {

    private static final long serialVersionUID = 149L;

    @NotNull
    @ViewMeta(order = 1)
    private String note;

    
    @ViewMeta(order = 4,
              formField = false)
    private LocalDate dueDate;
    
    
    @ViewMeta(formField = false)
    private LocalDateTime completedDate;
    
    
    @NotNull
    @ViewMeta(order = 2,
              formField = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;
    
    
    @ViewMeta(formField = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTACT_ID")
    private Contact contact;

    
    @NotNull
    @ViewMeta(formField = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", nullable = false)
    private User createdUser;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSIGNED_TO")
    private User assignedUser;

    
    // ----------------------------------------------------- constructors

    public Task() { }
    
    
    public Task(String note, Contact contact, User createdUser, User assignedUser) {
        this.note = note;
        this.contact = contact;
        this.createdUser = createdUser;
        this.assignedUser = assignedUser;
        this.status = Status.OPEN;
    }
    
    
    
    public enum Status {
        OPEN,
        PENDING,
        COMPLETED
    }

    
    
    // ----------------------------------------------------- setters and getters

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

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

}
