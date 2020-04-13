package com.customer.syn.resource.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Comment extends BaseEntity<Long> {

    private static final long serialVersionUID = 96L;

    @Basic
    @Column(nullable = true)
    private String commentText;

}
