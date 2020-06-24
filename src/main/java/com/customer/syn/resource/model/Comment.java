package com.customer.syn.resource.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Comment extends BaseEntity<Long> implements Serializable {

    private static final long serialVersionUID = 96L;

    @Basic
    @Column(nullable = false)
    private String commentText;

}
