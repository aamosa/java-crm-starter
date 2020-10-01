package com.customer.syn.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.EnumType.STRING;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Task extends BaseEntity<Long> implements Serializable {

    private static final long serialVersionUID = 149L;

    @NotNull
    @ViewMeta(order = 1)
    private String note;
    
    @ViewMeta(order = 3, formField = false)
    private LocalDate dueDate;
    
    @ViewMeta(order = 4, formField = false)
    private LocalDateTime completedDate;
    
    @NotNull
    @ViewMeta(order = 2, formField = false)
    @Enumerated(STRING)
    private Status status = Status.OPEN;
    
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CONTACT_ID")
    private Contact contact;
    
    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CREATED_BY", nullable = false)
    private User createdBy;
    
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ASSIGNED_TO")
    private User assignedTo;

    
    // ------------------------------------------------------------------ constructors
    public Task() { /* no-args constructor */ }


    public Task(String note, Contact contact, User createdUser, User assignedUser) {
        this.note = note;
        this.contact = contact;
        this.createdBy = createdUser;
        this.assignedTo = assignedUser;
        this.status = Status.OPEN;
    }
    
    
    public enum Status {
        OPEN,
        PENDING,
        COMPLETED
    }
    
    
    // ------------------------------------------------------------------ setters and getters
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
        return createdBy;
    }

    public void setCreatedUser(User createdUser) {
        this.createdBy = createdUser;
    }

    public User getAssignedUser() {
        return assignedTo;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedTo = assignedUser;
    }
    

}
