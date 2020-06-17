package com.customer.syn.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.resource.model.Task;
import com.customer.syn.resource.model.User;

@Stateless
public class TaskService extends BaseRepositoryImpl<Task, Long> {
    
    private static final Logger log = Logger.getLogger(TaskService.class.getName());
    
    
    public void save(Task task, Long contactId, Integer createdUserId, Integer assignedUserId) {
        try {
            Contact contact = em.find(Contact.class, contactId);
            User createdUser = em.find(User.class, createdUserId);
            User assignedUser = em.find(User.class, assignedUserId);

            task.setContact(contact);
            task.setCreatedUser(createdUser);
            task.setAssignedUser(assignedUser);
            super.save(task);
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
    }
    
}
