package com.customer.syn.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseEntity<ID extends Number & Serializable> {

    @Column(updatable=false)
    @ViewMeta(order=1, formField=false)
    @Id @GeneratedValue(strategy=IDENTITY)
    protected ID id;

//    @Version
//    protected I version;
    
    @ViewMeta(formField=false)
    @Column(nullable=false, updatable=false)
    protected Instant createdAt;

    @ViewMeta(formField=false)
    @Column(nullable= false)
    protected Instant updatedAt;

    // ------------------------------------------------------- lifecycle hooks
    @PrePersist
    public void onPersist() {
        setCreatedAt(Instant.now());
        setUpdatedAt(Instant.now());
    }

    @PreUpdate
    public void onUpdate() {
        setUpdatedAt(Instant.now());
    }

    /* Note: default hashCode based on the surrogate Id */
    @Override
    public int hashCode() {
        return getId() != null ? Objects.hash(getId()) : super.hashCode(); 
    }
    
    /* Note: default equality based on the surrogate Id */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        return getId().equals( ((BaseEntity<?>) o).getId());
    }

    // ------------------------------------------------------- setters and getters
//    public I getVersion() {
//        return this.version;
//    }
//
//    protected void setVersion(I version) {
//        this.version = version;
//    }
    
    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

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
