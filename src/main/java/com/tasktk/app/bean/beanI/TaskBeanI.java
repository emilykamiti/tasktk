package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Activity;
import com.tasktk.app.entity.Task;

import java.sql.SQLException;

public interface TaskBeanI extends GenericBeanI<Task>{
    Task createTask (Task task) throws SQLException;

    boolean updateTask(Task task) throws SQLException;

    boolean deleteTask(Task task);

    Task findById(Long taskId);
}
