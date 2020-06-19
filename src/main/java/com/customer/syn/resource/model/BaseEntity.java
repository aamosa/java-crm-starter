package com.customer.syn.resource.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseEntity<I extends Number & Comparable<I>> implements Serializable {

    private static final long serialVersionUID = -3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected I Id;
    

//    @Version
//    protected I version;
    
    
    @ViewMeta(formField = false)
    @Column(nullable = false,
            updatable = false)
    protected Instant createdAt;

    
    @ViewMeta(formField = false)
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
    
    
    /** Default hashCode based on the generated Id */
    @Override
    public int hashCode() {
        return getId() != null ? Objects.hash(getId()) : super.hashCode(); 
    }
    
    
    /** Default equality based on the generated Id */
    @Override
    public boolean equals(Object otherEntity) {
        if (this == otherEntity) return true;
        if ( !(getClass().isInstance(otherEntity) && otherEntity.getClass().isInstance(this)) ) return false;
        return getId().equals( ((BaseEntity<?>) otherEntity).getId());
    }
    
  
    
    // ------------------------------------------------- getters and setters

//    public I getVersion() {
//        return this.version;
//    }
//
//    protected void setVersion(I version) {
//        this.version = version;
//    }
    
    public I getId() {
        return Id;
    }

    protected void setId(I id) {
        Id = id;
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
