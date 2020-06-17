package com.customer.syn.view.task;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.customer.syn.resource.model.Contact;
import com.customer.syn.resource.model.Task;
import com.customer.syn.resource.model.User;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.service.TaskService;
import com.customer.syn.view.AbstractBacking;

@Named
@ViewScoped
public class TaskBacking extends AbstractBacking<Task, Long> implements Serializable {

    private static final long serialVersionUID = 5691L;
    private static final Logger log = Logger.getLogger(TaskBacking.class.getName());

    private Task task;
    private Long contactId;
    private Integer createdUserId;
    private Integer assignedUserId;

    @Inject
    private TaskService taskService;
    

    // -------------------------------------------------------- constructors

    public TaskBacking() { }

    
    @Override
    protected BaseRepositoryImpl<Task, Long> getService() {
        return taskService;
    }

    
    public void initialize() {
        task = new Task();
    }
    
    
    public String save() {
        taskService.save(task, contactId, createdUserId, assignedUserId);
        addMsg("Task created successfully!");
        return "task?faces-redirect=true";
    }
    

    
    // -------------------------------------------------------- setters and getters

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Integer getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Integer assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

}
