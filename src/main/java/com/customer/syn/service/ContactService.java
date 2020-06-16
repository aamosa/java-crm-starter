package com.customer.syn.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.customer.syn.resource.model.Contact;

@Stateless
public class ContactService extends BaseRepositoryImpl<Contact, Long> {
    
}