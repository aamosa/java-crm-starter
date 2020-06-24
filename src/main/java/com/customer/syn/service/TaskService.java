package com.customer.syn.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.resource.model.Task;
import com.customer.syn.resource.model.User;

@Stateless
public class TaskService extends BaseRepositoryImpl<Task, Long> {
    
    private static final Logger log = Logger.getLogger(TaskService.class.getName());
    
    @Inject
    private ContactService contactService;
    
    @Inject 
    private UserService userService;
    
    
    public void save(Task task, Long contactId, User loggedUser, User assigned) {
        try {
            task.setContact(contactService.findByID(contactId));
            task.setCreatedUser(userService.findByID(loggedUser.getId()));
            task.setAssignedUser(assigned);
            super.save(task);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    
    public Task TaskContactAndUsers(Long id) {
        return getEntityManager().createQuery(
                "select t from Task t join fetch t.contact join fetch t.createdUser join fetch t.assignedUser where t.id = :id",
                Task.class).setParameter("id", id).getSingleResult();
    }
    
    
}
