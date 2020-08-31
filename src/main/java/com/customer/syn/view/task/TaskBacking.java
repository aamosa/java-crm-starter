package com.customer.syn.view.task;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
    private static final Logger log = Logger.getLogger(TaskBacking.class.getName());

    private Task task;
    
    private Long contactId;

    private User assignedUser;
    
    
    @Inject @LoggedUser
    private User loggedUser;

    @Inject
    private TaskService taskService;
    
    @Inject
    private FacesContext facesContext;

    

    // ------------------------------------------------------------------ constructors

    public TaskBacking() { }
    
    
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
        Long contact_Id = Long.valueOf(facesContext.getExternalContext().getRequestParameterMap().get("contactId"));
        log.log(Level.INFO, () -> " contact id is : " + contact_Id);
        taskService.save(task, contact_Id, loggedUser, assignedUser);
        addMsg("Task created successfully!");
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
