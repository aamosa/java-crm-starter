package com.customer.syn.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.EnumType.STRING;
import static com.customer.syn.model.FormInputType.SELECT;
import static com.customer.syn.model.FormInputType.DATE;
import static com.customer.syn.model.FormInputType.DATETIME;

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

    private static final long serialVersionUID = 18745327891271L;

    @ViewMeta(order=1)
    @NotNull private String note;
    
    @ViewMeta(order=3, type=DATE)
    private LocalDate dueDate;
    
    @ViewMeta(order=4, formField=false)
    private LocalDateTime completedDate;

    @ViewMeta(order=2, formField=false)
    @Enumerated(STRING)
    @NotNull private Status status = Status.NEW;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="contact_id")
    private Contact contact;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="created_by")
    private User createdBy;

    @ViewMeta(type=SELECT)
    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="assigned_to")
    private User assignedTo;


    public enum Status {
        NEW, OPEN, PENDING, COMPLETED
    }

    // ------------------------------------------------------------------ constructors
    public Task() { /* no-args constructor */ }


    public Task(String note,
                Contact contact,
                User createdUser,
                User assignedUser) {
        this.note = note;
        this.contact = contact;
        this.createdBy = createdUser;
        this.assignedTo = assignedUser;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User user) {
        this.assignedTo = user;
    }
    

}
