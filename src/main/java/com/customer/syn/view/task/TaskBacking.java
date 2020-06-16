package com.customer.syn.view.task;

import com.customer.syn.resource.model.Task;
import com.customer.syn.service.BaseRepositoryImpl;
import com.customer.syn.view.AbstractBacking;

public class TaskBacking extends AbstractBacking<Task, Long>{

    @Override
    protected BaseRepositoryImpl<Task, Long> getService() {
        return null;
    }

}
