package com.customer.syn.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.customer.syn.resource.model.Contact;

@Stateless
public class TestContactService extends BaseRepositoryService<Contact, Long> {
    
    private static final Logger log = Logger.getLogger(TestContactService.class.getName());

    @PersistenceContext(name = "syn")
    private EntityManager em;
    
    
    public TestContactService() {
        super(Contact.class);
        log.info("<< TestContactService instance created! >>");
    }

}
