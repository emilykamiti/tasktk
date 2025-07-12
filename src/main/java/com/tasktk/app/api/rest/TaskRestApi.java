package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.GenericBeanI;
import com.tasktk.app.bean.beanI.TaskBeanI;
import com.tasktk.app.entity.Task;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;

@Path("/task")
public class TaskRestApi extends BaseEntityRestApi<Task> {
    @EJB
    private TaskBeanI taskBean;

    @Override
    protected GenericBeanI<Task> getBean() {
        return taskBean;
    }

    @Override
    protected Class<Task> getEntityClass() {
        return Task.class;
    }
}