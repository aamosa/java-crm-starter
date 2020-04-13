package com.customer.syn.resource.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = -3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T Id;

    @Column(nullable = false, updatable = false)
    protected Instant createdAt;

    @Column(nullable = false)
    protected Instant updatedAt;

    @PrePersist
    public void onPersist() {
        setCreatedAt(Instant.now());
        setUpdatedAt(Instant.now());
    }

    @PreUpdate
    public void onUpdate() {
         setUpdatedAt(Instant.now());
    }

    public T getId() {
        return Id;
    }

    public void setId(T id) {
        Id = id;
    }

    public LocalDateTime getCreatedAt() {
        return LocalDateTime.ofInstant(createdAt, ZoneOffset.UTC);
    }

    public void setCreatedAt(Instant time) {
        this.createdAt = time;
    }

    public LocalDateTime getUpdatedAt() {
        return LocalDateTime.ofInstant(updatedAt, ZoneOffset.UTC);
    }

    public void setUpdatedAt (Instant time) {
        this.updatedAt = time;
    }

}
