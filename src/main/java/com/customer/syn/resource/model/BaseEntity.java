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
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = -3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T Id;

//    @Version
//    protected T version;

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

    protected void setId(T id) {
        Id = id;
    }

//    public T getVersion() {
//        return this.version;
//    }
//
//    protected void setVersion(T version) {
//        this.version = version;
//    }

    public LocalDateTime getCreatedAt() {
        return LocalDateTime.ofInstant(createdAt, ZoneOffset.UTC);
    }

    protected void setCreatedAt(Instant time) {
        this.createdAt = time;
    }

    public LocalDateTime getUpdatedAt() {
        return LocalDateTime.ofInstant(updatedAt, ZoneOffset.UTC);
    }

    public void setUpdatedAt(Instant time) {
        this.updatedAt = time;
    }

}