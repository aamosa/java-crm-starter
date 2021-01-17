package com.customer.syn.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Comment extends BaseEntity<Long> implements Serializable {

    private static final long serialVersionUID = 4575411275546L;

    @Column(nullable=false)
    private String commentText;


    // ------------------------------------------------ setters and getters
    private String getCommentText() {
        return this.commentText;
    }

    private void setCommentText(String value) {
        this.commentText = value;
    }

}
