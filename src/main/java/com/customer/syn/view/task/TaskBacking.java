package com.customer.syn.view.task;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Tuple;

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
    private String userName;
    private User assignedUser;
    private String assgnUserName;
    private Map<String, String> detailAttrs;

    @Inject private FacesContext fc;
    @Inject private TaskService taskService;
    @Inject @LoggedUser private User loggedUser;


    // ------------------------------------------------------------------ constructors
    public TaskBacking() { /* no-arg constructor */ }
    
    
    @PostConstruct
    public void init() {
        task = new Task();
    }

    
    @Override
    protected BaseRepositoryImpl<Task, Long> getService() {
        return taskService;
    }


    @Override
    protected void doSearch(String value) {
        /* TODO: */
    }

    
    @Override
    public void view() {
        // get first result
        Tuple t = taskService.getTasksDTOList(getCurrentSelected().getId()).get(0);
        detailAttrs = new LinkedHashMap<>();
        detailAttrs.put("Task Id", t.get("id").toString());
        detailAttrs.put("Note", t.get("note").toString());
        detailAttrs.put("Due Date", t.get("due").toString());
        detailAttrs.put("Completed Date", t.get("completed") != null ? t.get("completed").toString() : "");
        detailAttrs.put("Created By", t.get("userName").toString());
        detailAttrs.put("For Contact", t.get("contactName").toString());
        setPage("taskdetail");
    }
    
    
    @Override
    public void edit(Task task) {
        task = taskService.getTaskContactAndUsers(task.getId());
        super.edit(task);
    }
    

    public String save() {  // TOOD:
        Long contactId = Long.valueOf(fc.getExternalContext()
                .getRequestParameterMap().get("contactId"));
        if (log.isDebugEnabled()) {
            log.debug("[contact id = {}]", contactId);
        }
        taskService.save(task, contactId, loggedUser, assignedUser);
        return "task?faces-redirect=true";
    }

    
    // ------------------------------------------------------------------ setters and getters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAssgnUserName() {
        return assgnUserName;
    }

    public void setAssgnUserName(String assgnUserName) {
        this.assgnUserName = assgnUserName;
    }

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

    public Map<String, String> getDetailAttrs() {
        return detailAttrs;
    }

    public void setDetailAttrs(Map<String, String> detailAttrs) {
        this.detailAttrs = detailAttrs;
    }

    
}
