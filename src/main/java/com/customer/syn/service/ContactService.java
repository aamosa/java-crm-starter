package com.customer.syn.service;

import java.util.List;

import javax.ejb.Stateless;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.resource.model.Task;

@Stateless
public class ContactService extends BaseRepositoryImpl<Contact, Long> {

    
    public List<Task> getTaskforContact(Contact contact) {
        return getEntityManager()
                .createQuery("select t from Task t join fetch t.createdUser where t.contact.id = :id", Task.class)
                .setParameter("id", contact.getId())
                .getResultList();
    }
    
}