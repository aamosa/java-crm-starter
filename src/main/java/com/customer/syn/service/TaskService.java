package com.customer.syn.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import com.customer.syn.model.Task;
import com.customer.syn.model.User;

@Stateless
public class TaskService extends BaseRepositoryImpl<Task, Long> {
    
    @Inject private UserService userService;
    @Inject private ContactService contactService;
    
    private static final String QUERY = "select t from Task t"
            + " join fetch t.contact"
            + " join fetch t.createdBy"
            + " join fetch t.assignedTo"
            + " where t.id = :id";

    
    public Task getTaskContactAndUsers(Long id) {
        return getEntityManager().createQuery(QUERY, Task.class)
                .setParameter("id", id)
                .getSingleResult();
    }
    
    
    public void save(Task task, Long contactId, User loggedUser, User assigned) {
        try {
            task.setContact(contactService.findByID(contactId));
            task.setCreatedUser(userService.findByID(loggedUser.getId()));
            task.setAssignedUser(assigned);
            super.save(task);
        } 
        catch (Exception e) {
            log.error("{}", e);
        }
    }
    
    
    public List<Tuple> getTasksDTOList(Long id) {
        TypedQuery<Tuple> query = getEntityManager()
                .createQuery("select t.id as id, "
                            + " t.note as note,"
                            + " t.dueDate as due,"
                            + " t.completedDate as completed,"
                            + " concat(u.firstName, ' ', u.lastName) as userName,"
                            + " concat(c.firstName, ' ', c.lastName) as contactName"
                            + " from Task t join t.createdBy u join t.contact c"
                            + " where t.id = :id", Tuple.class)
                .setParameter("id", id);
        List<Tuple> dto = query.getResultList();
        return dto;
    }
    
    
    
}
