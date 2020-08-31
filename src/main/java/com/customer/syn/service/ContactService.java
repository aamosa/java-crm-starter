package com.customer.syn.service;


import java.util.List;

import javax.ejb.Stateless;

import com.customer.syn.model.Contact;
import com.customer.syn.model.Task;

@Stateless
public class ContactService extends BaseRepositoryImpl<Contact, Long> {

    private static final String TASK_SQL = "select t from Task t join fetch t.createdUser "
            + "where t.contact.id = :id";
    
    
    // ------------------------------------------------------------ business methods
    
    public List<Task> findTasksforContact(Contact contact) {
        return getEntityManager()
                .createQuery(TASK_SQL, Task.class)
                .setParameter("id", contact.getId())
                .getResultList();
    }
    
    
}