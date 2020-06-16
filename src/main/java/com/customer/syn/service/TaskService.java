package com.customer.syn.service;

import javax.ejb.Stateless;

import com.customer.syn.resource.model.Task;

@Stateless
public class TaskService extends BaseRepositoryImpl<Task, Long> {

}
