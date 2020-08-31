package com.customer.syn.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.customer.syn.model.Task;
import com.customer.syn.model.User;

@Stateless
public class TaskService extends BaseRepositoryImpl<Task, Long> {
    
    @Inject 
    private UserService userService;
    
    @Inject
    private ContactService contactService;
    
    private static final String QUERY = "select t from Task t join fetch t.contact "
            + "join fetch t.createdUser join fetch t.assignedUser where t.id = :id";
       
    
    // ------------------------------------------------------------ business methods
    
    public Task TaskContactAndUsers(Long id) {
        return getEntityManager().createQuery(QUERY,Task.class)
                .setParameter("id", id).getSingleResult();
    }
    
    
    public void save(Task task, Long contactId, User loggedUser, User assigned) {
        try {
            task.setContact(contactService.findByID(contactId));
            task.setCreatedUser(userService.findByID(loggedUser.getId()));
            task.setAssignedUser(assigned);
            super.save(task);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
    
}
