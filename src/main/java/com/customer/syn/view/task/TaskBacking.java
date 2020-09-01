package com.customer.syn.view.task;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customer.syn.model.Task;
import com.customer.syn.model.User;
import com.customer.syn.resource.LoggedUser;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.TaskService;
import com.customer.syn.view.AbstractBacking;


@Named
@ViewScoped
public class TaskBacking extends AbstractBacking<Task, Long> implements Serializable {

    private static final long serialVersionUID = 5691L;

    private Task task;
    private Long contactId;
    private User assignedUser;
    
    @Inject 
    @LoggedUser
    private User loggedUser;
    
    @Inject
    private FacesContext fc;
    
    @Inject
    private TaskService taskService;
    

    // ------------------------------------------------------------------ constructors

    public TaskBacking() {}
    
    
    @PostConstruct
    public void init() {
        task = new Task();
    }

    
    @Override
    protected BaseRepositoryImpl<Task, Long> getService() {
        return taskService;
    }

    
//    public void initialize() {
//        task = new Task();
//    }
    
    
    @Override
    public void edit(Task task) {
        // task = taskService.update(task);
        task = taskService.TaskContactAndUsers(task.getId());
        super.edit(task);
    }
    
    
    public String save() {
        Long contactId = Long.valueOf(fc.getExternalContext()
                .getRequestParameterMap().get("contactId"));
        if (log.isDebugEnabled()) {
            log.debug("contact id is {}.", contactId);
        }
        taskService.save(task, contactId, loggedUser, assignedUser);
        return "task?faces-redirect=true";
    }

    
    // ------------------------------------------------------------------ setters and getters

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
    

}
